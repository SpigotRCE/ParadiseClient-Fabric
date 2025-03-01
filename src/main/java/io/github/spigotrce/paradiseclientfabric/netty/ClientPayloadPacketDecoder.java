package io.github.spigotrce.paradiseclientfabric.netty;

import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.channel.PluginMessageEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.md_5.bungee.protocol.ProtocolConstants;
import net.md_5.bungee.protocol.packet.PluginMessage;
import net.minecraft.network.PacketByteBuf;

import java.util.List;

public class ClientPayloadPacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        PacketByteBuf b = Helper.byteBufToPacketBuf(ctx.alloc().buffer().writeBytes(in));
        if (PayloadRegistry.isValidPacket(ParadiseClient_Fabric.NETWORK_CONFIGURATION.protocolVersion, b.readVarInt()))
            if (decodePayload(b))
                return;
        out.add(in.resetReaderIndex().retain());
    }

    public boolean decodePayload(PacketByteBuf b) {
        PluginMessage message = new PluginMessage();
        message.read(b.asByteBuf(), ProtocolConstants.Direction.TO_CLIENT, ParadiseClient_Fabric.NETWORK_CONFIGURATION.protocolVersion);
        PluginMessageEvent event = new PluginMessageEvent(message.getTag(), new PacketByteBuf(Unpooled.buffer().writeBytes(message.getData())));
        try {
            ParadiseClient_Fabric.EVENT_MANAGER.fireEvent(event);
        } catch (Exception e) {
            Constants.LOGGER.error("Unable to fire PluginMessageEvent", e);
        }

        return !event.isCancel();
    }
}
