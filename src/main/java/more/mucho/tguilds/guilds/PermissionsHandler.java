package more.mucho.tguilds.guilds;

public interface PermissionsHandler {
    boolean canInvite(Member member);
    boolean canKick(Member member);
    boolean canSethome(Member member);
}


