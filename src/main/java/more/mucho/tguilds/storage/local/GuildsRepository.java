package more.mucho.tguilds.storage.local;

import more.mucho.tguilds.guilds.Guild;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface GuildsRepository {
    CompletableFuture<Optional<Guild>> getOrLoad(int guildId);
    CompletableFuture<Optional<Guild>> getOrLoadByName(String name);
    CompletableFuture<Optional<Guild>> getOrLoadByTag(String tag);

    CompletableFuture<Boolean> saveGuild(Guild guild);

    void deleteGuild(int guildId);

    GuildsCache cache();
}
