package more.mucho.tguilds.commands;

import more.mucho.tguilds.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    private final String[] commandNames;
    private final FileConfiguration messages = new ConfigHandler("messages.yml").getConfig();
    public AbstractCommand(String... commandNames) {
        this.commandNames = commandNames;
    }

    public abstract boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args);

    public abstract List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args);

    public String[] getCommandNames() {
        return commandNames;
    }

    public void sendMessage(CommandSender sender, String path) {
        String message = messages.getString(path);
        if(message == null||message.isEmpty())return;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }



    public int parseInt(String arg, int def) {
        try {
            return Integer.parseInt(arg);
        } catch (Exception ignored) {
            return def;
        }
    }
    public static int getPage(@Nonnull String arg) {
        int page = 0;
        try {
            page = Integer.parseInt(arg);
        } catch (Exception ingored) {
        }
        if (page < 0) page = 0;
        return page;
    }
}
