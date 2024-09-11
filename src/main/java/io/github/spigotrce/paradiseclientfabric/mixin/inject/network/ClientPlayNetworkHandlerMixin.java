package io.github.spigotrce.paradiseclientfabric.mixin.inject.network;

import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin class to modify the behavior of the ClientPlayNetworkHandler class.
 * <p>
 * This class handles the game join event and updates connection information.
 * </p>
 *
 * @author SpigotRCE
 * @since 2.17
 */
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    /**
     * Injects code at the end of the onGameJoin method to update connection status and server IP.
     * <p>
     * This method sets the connection status to true and updates the server IP address
     * when the game join packet is received.
     * </p>
     *
     * @param packet The game join packet received from the server.
     * @param info   The callback information.
     */
    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
        ParadiseClient_Fabric.getNetworkMod().isConnected = true;
        ParadiseClient_Fabric.getNetworkMod().serverIP = ((ClientPlayNetworkHandler) (Object) this).getConnection().getAddress().toString().split("/")[0];

        for (String channel : ParadiseClient_Fabric.getMiscMod().delayedMessages) {
            Helper.printChatMessage(channel);
            System.out.println(channel);
        }

        ParadiseClient_Fabric.getMiscMod().delayedMessages.clear();
    }
}
