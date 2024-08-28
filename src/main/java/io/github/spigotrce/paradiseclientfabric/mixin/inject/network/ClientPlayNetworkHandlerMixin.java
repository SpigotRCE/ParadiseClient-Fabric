package io.github.spigotrce.paradiseclientfabric.mixin.inject.network;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    /**
     * On game join
     *
     * @param packet Game join packet
     * @param info   Callback info
     */
    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
        ParadiseClient_Fabric.getNetworkMod().isConnected = true;
        ParadiseClient_Fabric.getNetworkMod().serverIP = ((ClientPlayNetworkHandler) (Object) this).getConnection().getAddress().toString().split("/")[0];
    }
}