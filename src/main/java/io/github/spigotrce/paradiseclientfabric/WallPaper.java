package io.github.spigotrce.paradiseclientfabric;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Random;

public class WallPaper {
    private static final Random random = new Random();
    private static final int[] drops = new int[300];

    public static void renderMatrix(DrawContext context, int width, int height) {
        context.fillGradient(0, 0, width, height, 0xCC000000, 0xCC000000);
        for (int i = 0; i < drops.length; i++) {
            String text = Helper.generateRandomString(1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", random);
            context.drawText(MinecraftClient.getInstance().textRenderer, text, i * 10, drops[i] * 10, 0x00FF00, false);

            if (drops[i] * 10 > height && random.nextDouble() > 0.975)
                drops[i] = 0;
            drops[i]++;
        }
    }
}
