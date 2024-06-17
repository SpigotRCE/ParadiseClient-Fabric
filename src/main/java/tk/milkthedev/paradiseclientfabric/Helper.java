package tk.milkthedev.paradiseclientfabric;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.Objects;

public class Helper
{
    public static Color getChroma(int delay, float saturation, float brightness)
    {
        double chroma = Math.ceil((double) (System.currentTimeMillis() + delay) / 20);
        chroma %= 360;
        return Color.getHSBColor((float) (chroma / 360), saturation, brightness);
    }

    public static void printChatMessage(String message)
    {
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

    public static void sendPacket(Packet packet) {Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(packet);}
}
