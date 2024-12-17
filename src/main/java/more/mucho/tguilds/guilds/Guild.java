package more.mucho.tguilds.guilds;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Guild {
    String getName();
    void setName(String name);
    String getTag();
    void setTag(String tag);
    Member getOwner();
    Set<Member> getMembers();
    Optional<Member> getMember(String name);
    Optional<Member> getMember(UUID memberUUID);

}
