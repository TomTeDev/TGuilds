package more.mucho.tguilds.utils;

public class GuilUtils {
    public static boolean isValidName(String name) {
        return name.length() >= Config.NAME_MIN_LENGTH && name.length() <= Config.NAME_MAX_LENGTH &&
                name.matches("[0-9a-zA-Z]+");
    }

    public static boolean isValidTag(String tag) {
        return tag.length() >= Config.TAG_MIN_LENGTH && tag.length() <= Config.TAG_MAX_LENGTH &&
                tag.matches("[0-9a-zA-Z]+");
    }
}
