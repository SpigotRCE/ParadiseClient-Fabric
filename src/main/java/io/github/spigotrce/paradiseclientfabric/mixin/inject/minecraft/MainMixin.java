package io.github.spigotrce.paradiseclientfabric.mixin.inject.minecraft;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MainMixin {
    @Inject(method = "main", at = @At(value = "HEAD"))
    private static void mainHead(String[] args, CallbackInfo ci) {
        System.setProperty("java.awt.headless", "false");
    }
}
