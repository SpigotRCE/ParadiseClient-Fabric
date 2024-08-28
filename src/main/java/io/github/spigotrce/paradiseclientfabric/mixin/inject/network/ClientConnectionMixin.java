package io.github.spigotrce.paradiseclientfabric.mixin.inject.network;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.PacketEvent;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void channelRead0Head(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        if (!PacketEvent.PreIncomingPacket(packet)) {
            ci.cancel();
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("TAIL"))
    public void channelRead0Tail(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        PacketEvent.PostIncomingPacket(packet);
    }

    @Inject(method = "sendImmediately", at = @At("HEAD"), cancellable = true)
    public void sendImmediatelyHead(Packet<?> packet, PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        if (!PacketEvent.PreOutgoingPacket(packet)) {
            ci.cancel();
        }
    }

    @Inject(method = "sendImmediately", at = @At("TAIL"))
    public void sendImmediatelyTail(Packet<?> packet, PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        PacketEvent.PostOutgoingPacket(packet);
    }

    @Inject(method = "disconnect(Lnet/minecraft/network/DisconnectionInfo;)V", at = @At("HEAD"))
    public void disconnectHead(DisconnectionInfo disconnectionInfo, CallbackInfo ci) {
        ParadiseClient_Fabric.getNetworkMod().isConnected = false;
    }
}
