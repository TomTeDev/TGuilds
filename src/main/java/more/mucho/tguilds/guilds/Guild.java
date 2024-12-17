package more.mucho.tguilds.guilds;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Guild {
    int getID();

    void setID(int ID);

    String getName();

    void setName(String name);

    String getTag();

    void setTag(String tag);

    Member getOwner();

    Set<Member> getMembers();

    Optional<Member> getMember(String name);

    Optional<Member> getMember(UUID memberUUID);

    boolean addMember(Member member);

    boolean removeMember(Member member);

    boolean promoteMember(Member member, RANK rank);

    boolean demoteMember(Member member, RANK rank);

}
