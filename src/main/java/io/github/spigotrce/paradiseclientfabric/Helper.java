package io.github.spigotrce.paradiseclientfabric;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Utility class providing various helper methods for Minecraft client operations.
 * <p>
 * This class includes methods for generating chroma colors, sending chat messages,
 * parsing and formatting colored text, and more.
 * </p>
 *
 * @author SpigotRCE
 * @since 1.0
 */
public class Helper {
    /**
     * Generates a chroma color based on the current time and given delay.
     *
     * @param delay      The delay in milliseconds to affect the color shift.
     * @param saturation The saturation of the color.
     * @param brightness The brightness of the color.
     * @return The generated {@link Color} object.
     */
    public static Color getChroma(int delay, float saturation, float brightness) {
        double chroma = Math.ceil((double) (System.currentTimeMillis() + delay) / 20);
        chroma %= 360;
        return Color.getHSBColor((float) (chroma / 360), saturation, brightness);
    }

    /**
     * Sends a chat message to the Minecraft player.
     *
     * @param message The message to be sent.
     */
    public static void printChatMessage(String message) {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(Text.of(message));
    }

    /**
     * Checks if a string can be parsed as a number.
     *
     * @param s The string to check.
     * @return {@code true} if the string is a valid number, {@code false} otherwise.
     */
    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Sends a network packet to the Minecraft server.
     *
     * @param packet The packet to be sent.
     */
    public static void sendPacket(Packet<?> packet) {
        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(packet);
    }

    /**
     * Parses a string message into a colored {@link Text} object.
     *
     * @param message The message to be parsed.
     * @return The formatted {@link Text} object.
     */
    public static Text parseColoredText(String message) {
        return parseColoredText(message, null);
    }

    /**
     * Parses a string message into a colored {@link Text} object with an optional click-to-copy action.
     *
     * @param message     The message to be parsed.
     * @param copyMessage The message to copy to the clipboard when clicked, or {@code null} for no action.
     * @return The formatted {@link Text} object.
     */
    public static Text parseColoredText(String message, String copyMessage) {
        MutableText text = Text.literal("");
        String[] parts = message.split("(?=&)");
        List<Formatting> currentFormats = new ArrayList<>();

        for (String part : parts) {
            if (part.isEmpty()) continue;
            if (part.startsWith("&")) {
                currentFormats.add(getColorFromCode(part.substring(0, 2)));
                String remaining = part.substring(2);
                if (!remaining.isEmpty()) {
                    MutableText formattedText = Text.literal(remaining);
                    for (Formatting format : currentFormats) {
                        formattedText = formattedText.formatted(format);
                    }
                    text.append(formattedText);
                }
            } else {
                MutableText unformattedText = Text.literal(part);
                for (Formatting format : currentFormats) {
                    unformattedText = unformattedText.formatted(format);
                }
                text.append(unformattedText);
            }
        }

        if (copyMessage != null && !copyMessage.isEmpty()) {
            text.setStyle(text.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyMessage)));
        }

        return text;
    }

    /**
     * Converts a color code string to a {@link Formatting} enum value.
     *
     * @param code The color code string (e.g., "&0", "&1").
     * @return The corresponding {@link Formatting} value.
     */
    private static Formatting getColorFromCode(String code) {
        return switch (code) {
            case "&0" -> Formatting.BLACK;
            case "&1" -> Formatting.DARK_BLUE;
            case "&2" -> Formatting.DARK_GREEN;
            case "&3" -> Formatting.DARK_AQUA;
            case "&4" -> Formatting.DARK_RED;
            case "&5" -> Formatting.DARK_PURPLE;
            case "&6" -> Formatting.GOLD;
            case "&7" -> Formatting.GRAY;
            case "&8" -> Formatting.DARK_GRAY;
            case "&9" -> Formatting.BLUE;
            case "&a" -> Formatting.GREEN;
            case "&b" -> Formatting.AQUA;
            case "&c" -> Formatting.RED;
            case "&d" -> Formatting.LIGHT_PURPLE;
            case "&e" -> Formatting.YELLOW;
            case "&f" -> Formatting.WHITE;
            case "&k" -> Formatting.OBFUSCATED;
            case "&l" -> Formatting.BOLD;
            case "&m" -> Formatting.STRIKETHROUGH;
            case "&n" -> Formatting.UNDERLINE;
            case "&o" -> Formatting.ITALIC;
            default -> Formatting.RESET;
        };
    }

    /**
     * Capitalizes the first letter of a string.
     *
     * @param str The string to capitalize.
     * @return The string with the first letter capitalized, or the original string if it is null or empty.
     */
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Generates a random string.
     *
     * @param length     The length of the created string.
     * @param characters The charset the generator will use.
     * @param random     The {@link Random} instance the generator will use.
     * @return The random string generated.
     */
    public static String generateRandomString(int length, String characters, Random random) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            result.append(characters.charAt(random.nextInt(characters.length())));
        return result.toString();
    }

    public static class ByteArrayOutput {
        private final ByteArrayDataOutput out;

        public ByteArrayOutput() {
            this.out = ByteStreams.newDataOutput();
        }

        public ByteArrayOutput(byte[] bytes) {
            this.out = ByteStreams.newDataOutput();
            out.write(bytes);
        }

        public ByteArrayDataOutput getBuf() {
            return out;
        }

        public byte[] toByteArray() {
            return out.toByteArray();
        }
    }
}
