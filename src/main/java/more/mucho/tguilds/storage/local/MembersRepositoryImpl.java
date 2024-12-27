package more.mucho.tguilds.storage.local;

import more.mucho.tguilds.guilds.Member;
import more.mucho.tguilds.storage.MembersDao;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MembersRepositoryImpl implements MembersRepository {

    private final MembersDao dao;
    private final MembersCache cache;

    public MembersRepositoryImpl(MembersDao dao, MembersCache cache) {
        this.dao = dao;
        this.cache = cache;
    }

    public CompletableFuture<Optional<Member>> getOrLoad(String memberName) {
        return CompletableFuture.supplyAsync(() -> cache.get(memberName))
                .thenCompose(optionalMember -> optionalMember
                        .map(member -> CompletableFuture.completedFuture(Optional.of(member)))
                        .orElseGet(() -> dao.getMember(memberName)
                                .thenApply(dbMember -> {
                                    dbMember.ifPresent(member -> cache.add(member, member.getID()));
                                    return dbMember;
                                })
                        )
                );
    }

    @Override
    public CompletableFuture<Boolean> delete(int memberID) {
        return dao.removeMember(memberID).thenApply(removed -> {
            if (removed) {
                cache.remove(memberID);
                return true;
            }
            return false;
        });
    }

    @Override
    public CompletableFuture<Boolean> save(Member member) {
        return dao.save(member).thenApply(id -> {
            if (id >= 0) {
                member.setID(id);
                cache.add(member, id);
                return true;
            }
            return false;
        });
    }


    @Override
    public boolean isLoaded(Player player) {
        return cache.get(player.getName()).isPresent();
    }

    public CompletableFuture<Set<Member>> getAndLoadAllGuildMembers(int guildID) {
        return dao.getAllGuildMembers(guildID).thenApply(members -> {
            cache.addAll(members, Member::getID);
            return members;
        });
    }

    public MembersCache cache() {
        return cache;
    }

}
