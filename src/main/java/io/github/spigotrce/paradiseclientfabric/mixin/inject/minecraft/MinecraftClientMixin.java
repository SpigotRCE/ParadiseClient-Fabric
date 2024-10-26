package io.github.spigotrce.paradiseclientfabric.mixin.inject.minecraft;

import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.discord.RPC;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin class to modify the behavior of the MinecraftClient class.
 * <p>
 * This class injects code into various methods of the MinecraftClient class
 * to customize the window title, handle client shutdown, start a Discord
 * RPC thread, and keep track of the currently displayed screen.
 * </p>
 *
 * @author SpigotRCE
 * @since 1.0
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    /**
     * Injects code to modify the window title returned by the getWindowTitle method.
     * <p>
     * This method sets the window title to include the ParadiseClient edition,
     * version, and the game version.
     * </p>
     *
     * @param callback Callback information for the return value.
     */
    @Inject(method = "getWindowTitle", at = @At(
            value = "INVOKE",
            target = "Ljava/lang/StringBuilder;append(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            ordinal = 1),
            cancellable = true)
    private void getClientTitle(CallbackInfoReturnable<String> callback) {
        callback.setReturnValue("ParadiseClient [" + Constants.EDITION + "] " + Constants.VERSION + "/" + SharedConstants.getGameVersion().getName());
    }

    /**
     * Injects code at the start of the close method to perform additional cleanup.
     * <p>
     * This method shuts down the chat room client when the Minecraft client is closed.
     * </p>
     *
     * @param ci Callback information.
     */
    @Inject(method = "close", at = @At(value = "HEAD"))
    private void closeHead(CallbackInfo ci) {
        ParadiseClient_Fabric.getChatRoomMod().client.shutdown();
    }

    /**
     * Injects code after the initialization of the MinecraftClient.
     * <p>
     * This method starts a new thread for the Discord RPC (Rich Presence Client).
     * </p>
     *
     * @param info Callback information.
     */

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo info) {
        new Thread(new RPC()).start();
        for (int i = 1; i < 10; i++)
            Constants.backgroundImages.add(Identifier.of(Constants.MOD_ID, i + ".png"));
    }

    /**
     * Injects code at the start of the setScreen method to track the currently displayed screen.
     * <p>
     * This method updates the current screen in the miscellaneous mod when a new screen is set.
     * </p>
     *
     * @param screen The screen being set.
     * @param ci     Callback information.
     */
    @Inject(method = "setScreen", at = @At(value = "HEAD"))
    private void setScreenHead(Screen screen, CallbackInfo ci) {
        ParadiseClient_Fabric.getMiscMod().currentScreen = screen;
    }
}
