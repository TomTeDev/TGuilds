package more.mucho.tguilds.utils;

import more.mucho.tguilds.guilds.Member;

public class Config {
    public static boolean allowViewOthers = false;
    public static boolean homesEnabled = false;
    public static int MAX_MEMBERS = 8;
    public static int NAME_MIN_LENGTH = 3;
    public static int NAME_MAX_LENGTH = 16;
    public static int TAG_MIN_LENGTH = 4;
    public static int TAG_MAX_LENGTH = 4;

    public static String formatGuildChatMessage(Member member, String message) {
        if (message == null || message.isEmpty()) return message;
        String format = "%rank% %name%: %message%";
        format = format.replaceAll("%name%", member.getName());
        format = format.replace("%rank%", member.getRank().name());
        format = format.replace("%message%", message);
        return TextUtils.color(format);
    }
}
