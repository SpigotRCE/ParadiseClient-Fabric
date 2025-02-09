package io.github.spigotrce.paradiseclientfabric.netty;

import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;

import java.nio.charset.Charset;
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
        ParadiseClient_Fabric.selectedProtocolVersion.protocolVersion = b.readVarInt();
    }
}
