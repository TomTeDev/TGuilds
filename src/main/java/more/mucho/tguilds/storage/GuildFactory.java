package more.mucho.tguilds.storage;

import more.mucho.tguilds.guilds.Guild;
import more.mucho.tguilds.guilds.GuildImpl;
import more.mucho.tguilds.storage.local.MembersRepository;

import java.util.UUID;

public class GuildFactory {
    public static Guild create(String type, String name, String tag, UUID guildID, MembersRepository membersRepository){
        return switch (type.toLowerCase()){
            case "default"->{
                yield new GuildImpl(-1,name,tag, guildID, membersRepository);
            }
            default -> {
                throw new RuntimeException("Unable to create guild of type " + type);
            }
        };
    }
}
