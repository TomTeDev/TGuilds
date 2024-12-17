package more.mucho.tguilds.storage;

import more.mucho.tguilds.guilds.Member;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MembersDao {
    CompletableFuture<Boolean> removeMember(int ID);
    CompletableFuture<Optional<Member>> getMember(int ID);
    CompletableFuture<Optional<Member>> getMember(String memberName);
    CompletableFuture<Optional<Member>> getMember(UUID memberUUID);
   CompletableFuture<Set<Member>> getAllGuildMembers(int guildID);
}
