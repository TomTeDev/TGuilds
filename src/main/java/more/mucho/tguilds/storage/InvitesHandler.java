package more.mucho.tguilds.storage;

public interface InvitesHandler {
     boolean isInvited(String name);
     void addInvited(String name);
     void removeInvited(String name);
}
