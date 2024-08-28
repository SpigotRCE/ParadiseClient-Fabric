package io.github.spigotrce.paradiseclientfabric;

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

public class Helper {
    public static Color getChroma(int delay, float saturation, float brightness) {
        double chroma = Math.ceil((double) (System.currentTimeMillis() + delay) / 20);
        chroma %= 360;
        return Color.getHSBColor((float) (chroma / 360), saturation, brightness);
    }

    public static void printChatMessage(String message) {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(Text.of(message));
    }

    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void sendPacket(Packet packet) {
        Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(packet);
    }

    public static Text parseColoredText(String message) {
        return parseColoredText(message, null);
    }

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

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
