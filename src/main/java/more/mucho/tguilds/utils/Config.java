package more.mucho.tguilds.utils;

import more.mucho.tguilds.guilds.Member;

public class Config {
    public static boolean allowViewOthers;
    public static boolean homesEnabled;
    public static int MAX_MEMBERS;

    public static String formatGuildChatMessage(Member member, String message) {
        if (message == null || message.isEmpty()) return message;
        String format = "%rank% %name%: %message%";
        format = format.replaceAll("%name%", member.getName());
        format = format.replace("%rank%", member.getRank().name());
        format = format.replace("%message%", message);
        return TextUtils.color(format);
    }
}
