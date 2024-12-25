package more.mucho.tguilds.utils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    // Regex patterns
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:#([a-fA-F0-9]{6}):#([a-fA-F0-9]{6})>(.*?)</gradient>");
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("#([a-fA-F0-9]{6})");
    private static final Pattern BUKKIT_COLOR_PATTERN = Pattern.compile("&([0-9a-fA-Fk-oK-OrR])");

    // Interpolate a color
    private static String interpolateColor(String startColor, String endColor, float ratio) {
        int start = Integer.parseInt(startColor, 16);
        int end = Integer.parseInt(endColor, 16);

        int r = (int) ((start >> 16 & 0xFF) + ((end >> 16 & 0xFF) - (start >> 16 & 0xFF)) * ratio);
        int g = (int) ((start >> 8 & 0xFF) + ((end >> 8 & 0xFF) - (start >> 8 & 0xFF)) * ratio);
        int b = (int) ((start & 0xFF) + ((end & 0xFF) - (start & 0xFF)) * ratio);

        return String.format("#%02X%02X%02X", r, g, b);
    }

    // Apply gradient
    private static String applyGradient(String text, String startColor, String endColor) {
        StringBuilder builder = new StringBuilder();
        int length = text.length();

        for (int i = 0; i < length; i++) {
            float ratio = (float) i / Math.max(length - 1, 1);
            String color = interpolateColor(startColor, endColor, ratio);

            // Minecraft's §x color format for hex
            builder.append("§x");
            for (char c : color.substring(1).toCharArray()) { // Skip '#'
                builder.append("§").append(c);
            }

            builder.append(text.charAt(i));
        }

        return builder.toString();
    }

    // Process a single lore line
    public static String color(String line) {
        String processedLine = line;

        // Process gradients
        Matcher gradientMatcher = GRADIENT_PATTERN.matcher(processedLine);
        while (gradientMatcher.find()) {
            String startColor = gradientMatcher.group(1);
            String endColor = gradientMatcher.group(2);
            String text = gradientMatcher.group(3);

            String gradientText = applyGradient(text, startColor, endColor);
            processedLine = processedLine.replace(gradientMatcher.group(0), gradientText);
        }

        // Process hex colors
        Matcher hexMatcher = HEX_COLOR_PATTERN.matcher(processedLine);
        while (hexMatcher.find()) {
            String color = hexMatcher.group(1);
            String hexCode = "§x";
            for (char c : color.toCharArray()) {
                hexCode += "§" + c;
            }

            processedLine = processedLine.replace("#" + color, hexCode);
        }

        // Process Bukkit color codes
        Matcher bukkitMatcher = BUKKIT_COLOR_PATTERN.matcher(processedLine);
        while (bukkitMatcher.find()) {
            String code = bukkitMatcher.group(1);
            processedLine = processedLine.replace("&" + code, "§" + code);
        }

        return processedLine;
    }

    // Process the full lore
    public static List<String> color(List<String> configLore) {
        List<String> processedLore = new ArrayList<>();
        for (String line : configLore) {
            processedLore.add(color(line));
        }
        return processedLore;
    }

    public static String feedPlaceholders(String text, Map.Entry<String, String>... placeholders) {
        //   new AbstractMap.SimpleEntry<String, String>("t","t");
        String out = text;
        for (Map.Entry<String, String> placeholder : placeholders) {
            out = out.replace(placeholder.getKey(), placeholder.getValue());
        }
        return out;
    }

    public static List<String> feedPlaceholders(List<String> list, Map.Entry<String, String>... placeholders) {
        List<String> out = new ArrayList<>();
        for (String line : list) {
            out.add(feedPlaceholders(line, placeholders));
        }
        return out;
    }

}
