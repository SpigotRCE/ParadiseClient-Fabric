package io.github.spigotrce.paradiseclientfabric.netty;

import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.channel.PluginMessageEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.PacketByteBuf;

import java.util.List;

public class ClientPayloadPacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        PacketByteBuf b = Helper.byteBufToPacketBuf(ctx.alloc().buffer().writeBytes(in));
        int id = b.readVarInt();
        if (PayloadRegistry.isValidPacket(ParadiseClient_Fabric.selectedProtocolVersion.protocolVersion, id))
            if (decodePayload(b)) {
                return;
            }
        out.add(in.resetReaderIndex().retain());
    }

    public boolean decodePayload(PacketByteBuf b) {
        PluginMessageEvent event = new PluginMessageEvent(b.readString(), b);
        try {
            ParadiseClient_Fabric.eventManager.fireEvent(event);
        } catch (Exception e) {
            Constants.LOGGER.error("Unable to fire PluginMessageEvent", e);
        }

        return !event.isCancel();
    }
}
