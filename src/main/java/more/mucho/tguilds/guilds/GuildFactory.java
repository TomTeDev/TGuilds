package more.mucho.tguilds.guilds;

import more.mucho.tguilds.storage.local.MembersRepository;
import org.bukkit.Location;


import java.util.UUID;

public class GuildFactory {
    public static Guild create(String type, String name, String tag, UUID guildID, Location home, MembersRepository membersRepository){
        return switch (type.toLowerCase()){
            case "default"->{
                yield new GuildImpl(-1,name,tag, guildID, home, membersRepository);
            }
            default -> {
                throw new RuntimeException("Unable to create guild of type " + type);
            }
        };
    }
}
