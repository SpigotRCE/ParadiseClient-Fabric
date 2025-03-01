package io.github.spigotrce.paradiseclientfabric.mixin.inject.network.connection;

import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.packet.incoming.PacketIncomingPostEvent;
import io.github.spigotrce.paradiseclientfabric.event.packet.incoming.PacketIncomingPreEvent;
import io.github.spigotrce.paradiseclientfabric.event.packet.outgoing.PacketOutgoingPostEvent;
import io.github.spigotrce.paradiseclientfabric.event.packet.outgoing.PacketOutgoingPreEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.network.NetworkState;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin class to modify the behavior of the ClientConnection class.
 * <p>
 * This class intercepts packet reading and sending operations to allow for custom
 * packet handling and event triggering. It also updates connection status on disconnection.
 * </p>
 *
 * @author SpigotRCE
 * @since 1.1
 */
@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    /**
     * Injects code at the start of the channelRead0 method to handle incoming packets.
     * <p>
     * This method cancels the processing of the packet if the PacketIncomingPreEvent event returns false.
     * </p>
     *
     * @param channelHandlerContext The Netty channel handler context.
     * @param packet                The incoming packet.
     * @param ci                    Callback information.
     */
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void channelRead0Head(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        PacketIncomingPreEvent event = new PacketIncomingPreEvent(packet);

        try {
            ParadiseClient_Fabric.EVENT_MANAGER.fireEvent(event);
        } catch (Exception e) {
            Constants.LOGGER.error("Unable to fire PacketIncomingPreEvent", e);
            return;
        }
        if (event.isCancel())
            ci.cancel();
    }

    /**
     * Injects code at the end of the channelRead0 method to handle post-processing of incoming packets.
     * <p>
     * This method triggers the PacketIncomingPostEvent event after the packet has been processed.
     * </p>
     *
     * @param channelHandlerContext The Netty channel handler context.
     * @param packet                The incoming packet.
     * @param ci                    Callback information.
     */
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("TAIL"))
    public void channelRead0Tail(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        PacketIncomingPostEvent event = new PacketIncomingPostEvent(packet);

        try {
            ParadiseClient_Fabric.EVENT_MANAGER.fireEvent(event);
        } catch (Exception e) {
            Constants.LOGGER.error("Unable to fire PacketIncomingPostEvent", e);
        }
    }

    /**
     * Injects code at the start of the sendImmediately method to handle outgoing packets.
     * <p>
     * This method cancels the sending of the packet if the PacketOutgoingPreEvent event returns false.
     * </p>
     *
     * @param packet    The outgoing packet.
     * @param callbacks The packet callbacks.
     * @param flush     Whether to flush the packet.
     * @param ci        Callback information.
     */
    @Inject(method = "sendImmediately", at = @At("HEAD"), cancellable = true)
    public void sendImmediatelyHead(Packet<?> packet, PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        PacketOutgoingPreEvent event = new PacketOutgoingPreEvent(packet);

        try {
            ParadiseClient_Fabric.EVENT_MANAGER.fireEvent(event);
        } catch (Exception e) {
            Constants.LOGGER.error("Unable to fire PacketOutgoingPreEvent", e);
            return;
        }
        if (event.isCancel())
            ci.cancel();
    }

    /**
     * Injects code at the end of the sendImmediately method to handle post-processing of outgoing packets.
     * <p>
     * This method triggers the PacketOutgoingPostEvent event after the packet has been sent.
     * </p>
     *
     * @param packet    The outgoing packet.
     * @param callbacks The packet callbacks.
     * @param flush     Whether to flush the packet.
     * @param ci        Callback information.
     */
    @Inject(method = "sendImmediately", at = @At("TAIL"))
    public void sendImmediatelyTail(Packet<?> packet, PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        PacketOutgoingPostEvent event = new PacketOutgoingPostEvent(packet);

        try {
            ParadiseClient_Fabric.EVENT_MANAGER.fireEvent(event);
        } catch (Exception e) {
            Constants.LOGGER.error("Unable to fire PacketOutgoingPostEvent", e);
        }
    }

    /**
     * Injects code at the start of the disconnect method to handle disconnection events.
     * <p>
     * This method updates the connection status in the network mod to indicate disconnection.
     * </p>
     *
     * @param disconnectionInfo The disconnection information.
     * @param ci                Callback information.
     */
    @Inject(method = "disconnect(Lnet/minecraft/network/DisconnectionInfo;)V", at = @At("HEAD"))
    public void disconnectHead(DisconnectionInfo disconnectionInfo, CallbackInfo ci) {
        ParadiseClient_Fabric.NETWORK_MOD.isConnected = false;
    }

    @Inject(method = "transitionInbound", at = @At("HEAD"))
    public <T extends PacketListener> void transitionInbound(NetworkState<T> state, T packetListener, CallbackInfo ci) {
        ParadiseClient_Fabric.NETWORK_CONFIGURATION.state = state;
    }
}
