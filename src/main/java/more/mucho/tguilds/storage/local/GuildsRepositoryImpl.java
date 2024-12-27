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

    @Override
    public CompletableFuture<Optional<Guild>> getOrLoadByName(String name) {
        return CompletableFuture.supplyAsync(() -> guildsCache.getByName(name))
                .thenCompose(optionalGuild -> optionalGuild
                        .map(guild -> CompletableFuture.completedFuture(Optional.of(guild)))
                        .orElseGet(() -> guildDao.getByName(name)
                                .thenApply(dbGuild -> {
                                    dbGuild.ifPresent(guild -> guildsCache.add(guild, guild.getID()));
                                    return dbGuild;
                                })
                        )
                );
    }
    @Override
    public CompletableFuture<Optional<Guild>> getOrLoadByTag(String tag) {
        return CompletableFuture.supplyAsync(() -> guildsCache.getByTag(tag))
                .thenCompose(optionalGuild -> optionalGuild
                        .map(guild -> CompletableFuture.completedFuture(Optional.of(guild)))
                        .orElseGet(() -> guildDao.getByTag(tag)
                                .thenApply(dbGuild -> {
                                    dbGuild.ifPresent(guild -> guildsCache.add(guild, guild.getID()));
                                    return dbGuild;
                                })
                        )
                );
    }



    public CompletableFuture<Boolean> saveGuild(Guild guild) {
        return guildDao.addGuild(guild).thenApply(guildID -> {
            if (guildID < 0) return false;
            guild.setID(guildID);
            guildsCache.add(guild, guildID);
            return true;
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
