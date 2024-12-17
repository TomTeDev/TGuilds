package more.mucho.tguilds.data;



import more.mucho.tguilds.TGuilds;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigHandler {
    private final Plugin plugin = TGuilds.getPlugin(TGuilds.class);
    private FileConfiguration fileConfig = null;
    private File file = null;
    private final String fileName;

    public ConfigHandler(@NonNull String fileName) {
        this.fileName = fileName;
        //Create File
        saveDeafaultConfig();
        try {
            this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        if (this.file == null) {
            this.file = new File(this.plugin.getDataFolder(), fileName);
        }
        this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
        InputStream defaultStream = this.plugin.getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.fileConfig.setDefaults(yamlConfiguration);
        }
    }

    public FileConfiguration getConfig() {
        if (this.fileConfig == null) reloadConfig();
        return this.fileConfig;
    }

    public void saveConfig(FileConfiguration cfg) {
        if (this.file == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try{
                cfg.save(file);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private void saveDeafaultConfig() {
        if (this.file == null) {
            this.file = new File(this.plugin.getDataFolder(), fileName);
        }
        if (!this.file.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}
