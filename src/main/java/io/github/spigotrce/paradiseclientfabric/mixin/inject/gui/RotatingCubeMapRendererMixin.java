package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui;

import io.github.spigotrce.paradiseclientfabric.Constants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for the RotatingCubeMapRenderer class to customize the rendering.
 * This mixin replaces the default rendering with a custom background image.
 *
 * @author SpigotRCE
 * @since 2.20
 */
@Mixin(RotatingCubeMapRenderer.class)
public class RotatingCubeMapRendererMixin {

    /**
     * Injects custom rendering logic into the render method of RotatingCubeMapRenderer.
     * This method draws a custom background image and cancels the original render process.
     *
     * @param context   The draw context used for rendering.
     * @param width     The width of the rendering area.
     * @param height    The height of the rendering area.
     * @param alpha     Alpha transparency value (not used here).
     * @param tickDelta Delta time since the last tick (not used here).
     * @param ci        Callback information for the method.
     */
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int width, int height, float alpha, float tickDelta, CallbackInfo ci) {
        // Draw the custom texture over the entire screen
        context.drawTexture(Constants.backgroundImage, 0, 0, width, height, 0.0F, 0.0F, width, height, width, height);

        // Cancel the original rendering logic
        ci.cancel();
    }
}
