package more.mucho.tguilds.storage;

import more.mucho.tguilds.guilds.Guild;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface GuildsDao {
    CompletableFuture<Integer> addGuild(Guild guild);
    CompletableFuture<Boolean> removeGuild(int ID);
    CompletableFuture<Optional<Guild>>getGuild(int ID);
    CompletableFuture<Optional<Guild>>getByTag(String tag);
    CompletableFuture<Optional<Guild>>getByName(String name);
}
