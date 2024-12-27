package more.mucho.tguilds.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.CheckForNull;


public class LocationUtils {
    @CheckForNull
    public static Location fromString(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            String[] split = value.split(",");
            if (split.length < 4) {
                return null;
            }
            Location location = new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
            if (split.length > 5) {
                location.setYaw(Float.parseFloat(split[4]));
                location.setPitch(Float.parseFloat(split[5]));
            }
            return location;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }


    }

    public static String toString(Location location, boolean onlyBlock) {
        String value = location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
        if (!onlyBlock) {
            value += "," + location.getYaw() + "," + location.getPitch();
        }
        return value;
    }
}
