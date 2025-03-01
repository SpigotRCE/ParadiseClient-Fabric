package io.github.spigotrce.paradiseclientfabric.netty;

import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.md_5.bungee.protocol.packet.Handshake;
import net.minecraft.network.PacketByteBuf;

import java.util.List;

public class ClientHandshakeEncoder extends MessageToMessageEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        PacketByteBuf b = Helper.byteBufToPacketBuf(ctx.alloc().buffer().writeBytes(in));
        b.readVarInt(); // Packet id
        decodeHandshake(b); // decode handshake
        out.add(in.resetReaderIndex().retain()); // Forward to the next handler
        ctx.pipeline().remove(this); // Remove this handler
    }

    private void decodeHandshake(PacketByteBuf b) {
        Handshake handshake = new Handshake();
        handshake.read(b.asByteBuf());
        ParadiseClient_Fabric.SELECTED_PROTOCOL_VERSION.protocolVersion = handshake.getProtocolVersion();
    }
}
