package io.github.spigotrce.paradiseclientfabric.mixin.inject.minecraft;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.discord.RPC;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "getWindowTitle", at = @At(
            value = "INVOKE",
            target = "Ljava/lang/StringBuilder;append(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            ordinal = 1),
            cancellable = true)
    private void getClientTitle(CallbackInfoReturnable<String> callback) {
        callback.setReturnValue("ParadiseClient [" + Constants.EDITION + "]" + Constants.VERSION + "/" + SharedConstants.getGameVersion().getName());
    }

    @Inject(method = "close", at = @At(value = "HEAD"))
    private void closeHead(CallbackInfo ci) {
        ParadiseClient_Fabric.getChatRoomMod().client.shutdown();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo info) {
        new Thread(new RPC()).start();
    }

    @Inject(method = "setScreen", at = @At(value = "HEAD"))
    private void setScreenHead(Screen screen, CallbackInfo ci) {
        ParadiseClient_Fabric.getMiscMod().currentScreen = screen;
    }
}
