package more.mucho.tguilds.storage.local;

import more.mucho.tguilds.guilds.Guild;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface GuildsRepository {
    CompletableFuture<Optional<Guild>> getOrLoad(int guildId);

    void saveGuild(Guild guild);

    void deleteGuild(int guildId);

    GuildsCache cache();
}
