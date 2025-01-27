package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui;

import io.github.spigotrce.paradiseclientfabric.WallPaper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RotatingCubeMapRenderer.class)
public class RotatingCubeMapRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int width, int height, float alpha, float tickDelta, CallbackInfo ci) {
        // Appelle le rendu dynamique basé sur le thème défini
        WallPaper.render(context, width, height);
        ci.cancel(); // Annule le rendu original
    }
}
