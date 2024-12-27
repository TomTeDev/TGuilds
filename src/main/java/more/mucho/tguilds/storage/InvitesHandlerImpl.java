package more.mucho.tguilds.storage;

import more.mucho.tguilds.TGuilds;
import org.bukkit.Bukkit;

import java.util.HashSet;


public class InvitesHandlerImpl implements InvitesHandler {
    private final HashSet<String> invites = new HashSet<>();

    @Override
    public boolean isInvited(String name) {
        return invites.contains(name.toLowerCase());
    }

    @Override
    public void addInvited(String name) {
        invites.add(name.toLowerCase());
        Bukkit.getScheduler().runTaskLater(TGuilds.i(), () -> invites.remove(name.toLowerCase()), 20 * 60);
    }

    @Override
    public void removeInvited(String name) {
        invites.remove(name.toLowerCase());
    }
}
