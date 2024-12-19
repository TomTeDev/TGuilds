package more.mucho.tguilds.storage.local;

import more.mucho.tguilds.guilds.Guild;
import more.mucho.tguilds.storage.GuildsDao;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class GuildsRepositoryImpl implements GuildsRepository {
    private final GuildsDao guildDao;
    private final GuildsCache guildsCache;

    public GuildsRepositoryImpl(GuildsDao guildDao, GuildsCache guildsCache) {
        this.guildDao = guildDao;
        this.guildsCache = guildsCache;
    }

    public CompletableFuture<Optional<Guild>> getOrLoad(int guildId) {
        return CompletableFuture.supplyAsync(() -> guildsCache.get(guildId))
                .thenCompose(optionalGuild -> optionalGuild
                        .map(guild -> CompletableFuture.completedFuture(Optional.of(guild)))
                        .orElseGet(() -> guildDao.getGuild(guildId)
                                .thenApply(dbGuild -> {
                                    dbGuild.ifPresent(guild -> guildsCache.add(guild, guildId));
                                    return dbGuild;
                                })
                        )
                );
    }

    public void saveGuild(Guild guild) {
        guildDao.addGuild(guild).thenAccept(guildID -> {
            if (guildID < 0) return;
            guild.setID(guildID);
            guildsCache.add(guild, guildID);
        });
    }

    public void deleteGuild(int guildId) {
        guildDao.removeGuild(guildId).thenAccept(removed -> {
            if (!removed) return;
            guildsCache.remove(guildId);
        });
    }

    public GuildsCache cache() {
        return guildsCache;
    }


}
