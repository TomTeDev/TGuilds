package more.mucho.tguilds.storage;

import more.mucho.tguilds.guilds.GuildImpl;
import more.mucho.tguilds.guilds.Member;
import more.mucho.tguilds.guilds.RANK;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MembersFactory {
    public static Member createMember(String type, String playerName, UUID playerUUID, RANK rank, int guildID) {
        return switch (type.toLowerCase()) {
              case "default"->{
                  yield new GuildImpl.MemberImpl(-1,playerName,playerUUID,rank,guildID);
              }
            default -> throw new RuntimeException("Unable to create member of type " + type);
        };
    }
}
