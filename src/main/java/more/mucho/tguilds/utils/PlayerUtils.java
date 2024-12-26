package more.mucho.tguilds.utils;

import more.mucho.tguilds.TGuilds;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class PlayerUtils {
    public static final String CHAT_TOGGLE_KEY = "m_guild_chat_toggled";

    public static void toggleChat(Player player) {
        boolean isToggled = isChatToggled(player);
        player.getPersistentDataContainer().set(new NamespacedKey(TGuilds.i(),CHAT_TOGGLE_KEY), PersistentDataType.BOOLEAN, !isToggled);
    }

    public static boolean isChatToggled(Player player) {
        return player.getPersistentDataContainer()
                .getOrDefault(new NamespacedKey(TGuilds.i(),CHAT_TOGGLE_KEY), PersistentDataType.BOOLEAN, false);
    }
}