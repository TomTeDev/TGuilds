package more.mucho.tguilds.storage.local;

import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface GuildService {

    CompletableFuture<Boolean> guildWithNameExists(String name);

    CompletableFuture<Boolean> guildWithTagExists(String tag);
}
