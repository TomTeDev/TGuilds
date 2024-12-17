package more.mucho.tguilds.guilds;

import java.util.UUID;

public interface Member {
    int getID();
    void setID(int ID);
    String getName();
    UUID getUUID();
    RANK getRank();
    void setRank(RANK rank);
    int getGuildID();
    void setGuildID(int guildID);
}
