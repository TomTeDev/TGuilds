package more.mucho.tguilds.listeners;

import more.mucho.tguilds.guilds.Guild;
import more.mucho.tguilds.guilds.Member;
import more.mucho.tguilds.storage.local.Repositories;
import more.mucho.tguilds.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!PlayerUtils.isChatToggled(event.getPlayer())) return;
        Player player = event.getPlayer();
        Optional<Member> member = Repositories.getInstance().getMembersRepository().cache().get(player.getName());
        if (member.isEmpty()) {
            PlayerUtils.toggleChat(player);
            return;
        }
        Optional<Guild> guild = Repositories.getInstance().getGuildsRepository().cache().get(member.get().getGuildID());
        if (guild.isEmpty()) {
            PlayerUtils.toggleChat(player);
            return;
        }
        event.setCancelled(true);
        guild.get().sendMessage(member.get(), event.getMessage());
    }
}
