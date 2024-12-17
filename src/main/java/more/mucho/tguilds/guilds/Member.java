package more.mucho.tguilds.guilds;

import java.util.UUID;

public interface Member {
    String getName();
    UUID getUUID();
    RANK getRank();
}
