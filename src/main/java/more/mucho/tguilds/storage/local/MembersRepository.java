package more.mucho.tguilds.storage.local;

import more.mucho.tguilds.guilds.Member;
import more.mucho.tguilds.storage.MembersDao;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface MembersRepository {

    CompletableFuture<Optional<Member>> getOrLoad(String playerName);

    boolean isLoaded(Player player);


    CompletableFuture<Set<Member>> getAndLoadAllGuildMembers(int guildID);
}
