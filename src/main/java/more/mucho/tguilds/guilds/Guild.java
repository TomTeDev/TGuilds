package more.mucho.tguilds.guilds;

import more.mucho.tguilds.storage.InvitesHandler;
import more.mucho.tguilds.storage.PermissionsHandler;
import org.bukkit.Location;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Guild {
    int getID();

    void setID(int ID);

    UUID getUUID();

    String getName();

    void setName(String name);

    String getTag();

    void setTag(String tag);
    void setHome(Location location);
    Optional<Location> getHome();
    Member getOwner();

    Set<Member> getMembers();
    boolean isMember(Member member);
    void sendMessage(Member member,String message);
    Optional<Member> getMember(String name);

    Optional<Member> getMember(UUID memberUUID);

    boolean addMember(Member member);

    boolean removeMember(Member member);

    boolean promoteMember(Member member, RANK rank);

    boolean demoteMember(Member member, RANK rank);
    InvitesHandler getInvitesHandler();
    PermissionsHandler getPermissionsHandler();
}
