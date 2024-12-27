package more.mucho.tguilds.commands;

import more.mucho.tguilds.data.ConfigHandler;
import more.mucho.tguilds.utils.TextUtils;
import more.mucho.tguilds.utils.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

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

/*    public void sendMessage(CommandSender sender, String path) {
        String message = messages.getString(path);
        if(message == null||message.isEmpty())return;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }*/
    @SafeVarargs
    public final void sendMessage(CommandSender sender, String path, Tuple<String, String>... entries) {
        String message = messages.getString(path);
        if(message == null||message.isEmpty())return;
        for(Tuple<String,String> entry : entries){
            message = message.replace(entry.getFirst(), entry.getSecond());
        }
        sender.sendMessage(TextUtils.color(message));
    }
    public void sendCorrectCommand(CommandSender sender,String path){
        sender.sendMessage(TextUtils.color(messages.getString("correct_commands."+path)));
    }
    public void sendMessages(CommandSender sender, String path) {
        List<String> messages = this.messages.getStringList(path);
        if(messages.isEmpty())return;
        String[]messagesArray = messages.stream().map(TextUtils::color).toArray(String[]::new);
        sender.sendMessage(messagesArray);
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
