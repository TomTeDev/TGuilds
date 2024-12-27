package more.mucho.tguilds.storage;

import more.mucho.tguilds.guilds.Member;
import more.mucho.tguilds.guilds.RANK;

public class PermissionsHandlerImpl implements PermissionsHandler{
    @Override
    public boolean canInvite(Member member) {
        return member.getRank().power>= RANK.VETERAN.power;
    }

    @Override
    public boolean canKick(Member member) {
        return member.getRank().power>= RANK.VETERAN.power;
    }

    @Override
    public boolean canSethome(Member member) {
        return member.getRank().power>= RANK.OFFICER.power;
    }
}
