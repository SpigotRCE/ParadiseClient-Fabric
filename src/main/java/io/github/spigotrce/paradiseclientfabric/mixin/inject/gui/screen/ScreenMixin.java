package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

/**
 * Mixin for the Screen class to customize background rendering.
 * This mixin replaces the default background for specific screens with a custom texture.
 *
 * @author SpigotRCE
 * @since 1.9
 */
@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow public int height;
    @Shadow public int width;

    @Shadow protected MinecraftClient client;
    @Unique private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    @Unique private final int[] drops = new int[300];
    @Unique private final Random random = new Random();

    /**
     * Injects custom background rendering into the renderBackground method.
     * This method draws a custom texture for specific screens and cancels the original rendering.
     *
     * @param context The draw context used for rendering.
     * @param mouseX  The X coordinate of the mouse.
     * @param mouseY  The Y coordinate of the mouse.
     * @param delta   The time delta since the last frame.
     * @param ci      The callback information for the method.
     */
    @Inject(method = "renderBackground", at = @At(value = "HEAD"), cancellable = true)
    private void renderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        context.fillGradient(0, 0, width, height, 0xCC000000, 0xCC000000);
        for (int i = 0; i < drops.length; i++) {
            String text = String.valueOf(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            context.drawText(this.client.textRenderer, text, i * 10, drops[i] * 10, 0x00FF00, false);

            if (drops[i] * 10 > height && random.nextDouble() > 0.975)
                drops[i] = 0;
            drops[i]++;
        }
        ci.cancel();
    }
}
