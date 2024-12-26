package more.mucho.tguilds.utils;

public class GuilUtils {
    public static boolean isValidName(String name) {
        return name.matches("[0-9a-zA-Z]+");
    }

    public static boolean isValidTag(String tag) {
        return tag.matches("[0-9a-zA-Z]+");
    }
}
