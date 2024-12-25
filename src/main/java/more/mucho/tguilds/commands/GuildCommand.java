package more.mucho.tguilds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class GuildCommand extends AbstractCommand {
    public GuildCommand(){
        super("guild");
    }
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if(args.length == 0){
            sendMessage(sender,"command.try_help");
            return false;
        }
        if(args[0].equalsIgnoreCase("help")){
            sendMessages(sender,"command.guild.help");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create"->{}
            case "add"->{}
            case "join"->{}
            case "kick"->{}
            case "leave"->{}
            case "info"->{}
            case "chat"->{}
            case "sethome"->{}
            case "home"->{}
            case "delete"->{}
            case "togglechat"->{}
        }




        return false;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        return null;
    }
}
