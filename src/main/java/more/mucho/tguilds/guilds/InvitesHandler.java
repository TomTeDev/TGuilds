package more.mucho.tguilds.guilds;

public interface InvitesHandler {
     boolean isInvited(String name);
     void addInvited(String name);
     void removeInvited(String name);
}
