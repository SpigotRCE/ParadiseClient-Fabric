package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui;

import io.github.spigotrce.paradiseclientfabric.Helper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(RotatingCubeMapRenderer.class)
public class RotatingCubeMapRendererMixin {
    @Shadow @Final private MinecraftClient client;
    @Unique private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    @Unique private final int[] drops = new int[300];
    @Unique private final Random random = new Random();

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int width, int height, float alpha, float tickDelta, CallbackInfo ci) {
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
