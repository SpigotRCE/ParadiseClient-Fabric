package io.github.spigotrce.paradiseclientfabric.mixin.inject.gui.screen;

import io.github.spigotrce.paradiseclientfabric.mixin.accessor.BungeeCordAccessor;
import net.md_5.bungee.BungeeCord;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public abstract class ConnectScreenMixin {
    @Shadow protected abstract void connect(MinecraftClient client, ServerAddress address, ServerInfo info, @Nullable CookieStorage cookieStorage);

    @Inject(method = "connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;Lnet/minecraft/client/network/CookieStorage;)V"
    , at = @At("HEAD"))
    private void c(MinecraftClient client, ServerAddress address, ServerInfo info, CookieStorage cookieStorage, CallbackInfo ci) {
        if (!address.equals(new ServerAddress("localhost", 25577))) {
            ((BungeeCordAccessor) BungeeCord.getInstance()).paradiseClient_Fabric$setTargetServer(address.getAddress(), address.getPort());

            this.connect(client, new ServerAddress("localhost", 25577), info, cookieStorage);
        }
    }
}
