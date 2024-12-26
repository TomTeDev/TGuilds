package more.mucho.tguilds.storage.local;

import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class GuildsServiceImpl implements GuildService {

    private final GuildsRepository guildsRepository;
    public GuildsServiceImpl(GuildsRepository guildsRepository) {
        this.guildsRepository = guildsRepository;
    }



    public CompletableFuture<Boolean> guildWithNameExists(String name) {
        return guildsRepository.cache().getGuildByName(name)
                .thenApply(Optional::isPresent);
    }

    public CompletableFuture<Boolean> guildWithTagExists(String tag) {
        return guildsRepository.cache().getGuildByTag(tag)
                .thenApply(Optional::isPresent);
    }
}