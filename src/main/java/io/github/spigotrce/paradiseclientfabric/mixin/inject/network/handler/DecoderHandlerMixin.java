package io.github.spigotrce.paradiseclientfabric.mixin.inject.network.handler;

import com.mojang.logging.LogUtils;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkState;
import net.minecraft.network.handler.DecoderHandler;
import net.minecraft.network.handler.NetworkStateTransitionHandler;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.util.profiling.jfr.FlightProfiler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;

import java.util.List;

@Mixin(DecoderHandler.class)
public class DecoderHandlerMixin<T extends PacketListener> {
    @Shadow
    private static final Logger LOGGER = LogUtils.getLogger();
    @Mutable
    @Final
    @Shadow
    private final NetworkState<T> state;

    @SuppressWarnings("unused")
    public DecoderHandlerMixin(NetworkState<T> state) {
        this.state = state;
    }


    /**
     * @author SpigotRCE
     * @reason To prevent disconnection issues
     */
    @Overwrite()
    public void decode(ChannelHandlerContext context, ByteBuf buf, List<Object> objects) {
        int i = buf.readableBytes();
        if (i != 0) {
            Packet<? super T> packet = this.state.codec().decode(buf);
            PacketType<? extends Packet<? super T>> packetType = packet.getPacketType();
            FlightProfiler.INSTANCE.onPacketReceived(this.state.id(), packetType, context.channel().remoteAddress(), i);
            if (buf.readableBytes() > 0)
                Helper.printChatMessage("&cError handling packet " + this.state.id().getId() + "/" + packetType + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + buf.readableBytes() + " bytes extra whilst reading packet " + packetType);
            else {
                objects.add(packet);
                NetworkStateTransitionHandler.onDecoded(context, packet);
            }
        }
    }
}
