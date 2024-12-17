package more.mucho.tguilds;

import org.bukkit.plugin.java.JavaPlugin;

public final class TGuilds extends JavaPlugin {

    private static TGuilds instance;

    @Override
    public void onEnable() {
        instance = this;


    }

    @Override
    public void onDisable() {

        instance = null;
    }
}
