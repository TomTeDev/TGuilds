package more.mucho.tguilds.storage;

import more.mucho.tguilds.guilds.Guild;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface GuildsDao {
    CompletableFuture<Boolean> addGuild(Guild guild);
    CompletableFuture<Boolean> removeGuild(int ID);
    CompletableFuture<Optional<Guild>>getGuild(int ID);
}