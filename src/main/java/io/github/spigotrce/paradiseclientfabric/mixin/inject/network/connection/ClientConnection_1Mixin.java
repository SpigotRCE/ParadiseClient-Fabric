package io.github.spigotrce.paradiseclientfabric.mixin.inject.network.connection;

import io.github.spigotrce.paradiseclientfabric.netty.NettyConstants;
import io.github.spigotrce.paradiseclientfabric.netty.ClientPayloadPacketDecoder;
import io.github.spigotrce.paradiseclientfabric.netty.ClientHandshakeEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.handler.HandlerNames;
import net.minecraft.network.handler.PacketSizeLogger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientConnection.class, priority = 1001)
public class ClientConnection_1Mixin {
    @Inject(method = "addHandlers", at = @At("RETURN"))
    private static void onAddHandlers(ChannelPipeline pipeline, NetworkSide side, boolean local, PacketSizeLogger packetSizeLogger, CallbackInfo ci) {
        if (pipeline.channel() instanceof SocketChannel) {
            pipeline.addBefore(HandlerNames.ENCODER, NettyConstants.PARADISE_HANDLER_ENCODER_NAME, new ClientHandshakeEncoder());
            pipeline.addBefore(HandlerNames.INBOUND_CONFIG , NettyConstants.PARADISE_HANDLER_DECODER_NAME, new ClientPayloadPacketDecoder());
        } else {
            System.out.println("Channel not an instance of netty socket");
        }
    }
}
