package more.mucho.tguilds.storage;

import more.mucho.tguilds.guilds.Member;

public interface PermissionsHandler {
    boolean canInvite(Member member);
    boolean canKick(Member member);
    boolean canSethome(Member member);
}


