package more.mucho.tguilds.listeners;

import more.mucho.tguilds.storage.local.GuildsRepository;
import more.mucho.tguilds.storage.local.MembersRepository;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final GuildsRepository guildsRepository;
    private final MembersRepository membersRepository;

    public JoinListener(GuildsRepository guildsRepository, MembersRepository membersRepository) {
        this.guildsRepository = guildsRepository;
        this.membersRepository = membersRepository;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (membersRepository.isLoaded(event.getPlayer())) return;
        membersRepository.getOrLoad(event.getPlayer().getName()).thenAccept(member -> {
            if (member.isEmpty()) return;
            guildsRepository.getOrLoad(member.get().getGuildID());
        });
    }
}
