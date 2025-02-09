package io.github.spigotrce.paradiseclientfabric.mixin.inject.network.coder;

import com.mojang.logging.LogUtils;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.network.handler.DecoderHandler;
import net.minecraft.network.handler.NetworkStateTransitionHandler;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.util.profiling.jfr.FlightProfiler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "decode", at = @At("HEAD"), cancellable = true)
    public void decode(ChannelHandlerContext context, ByteBuf buf, List<Object> objects, CallbackInfo ci) {
        int i = buf.readableBytes();
        if (i != 0) {
            Packet<? super T> packet = this.state.codec().decode(buf);
            PacketType<? extends Packet<? super T>> packetType = packet.getPacketType();
            FlightProfiler.INSTANCE.onPacketReceived(this.state.id(), packetType, context.channel().remoteAddress(), i);
            if (buf.readableBytes() > 0) {
                String var10002 = this.state.id().getId();
                Helper.printChatMessage("&cError handling packet " + var10002 + "/" + packetType + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + buf.readableBytes() + " bytes extra whilst reading packet " + packetType);
//                throw new IOException("Packet " + var10002 + "/" + packetType + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + buf.readableBytes() + " bytes extra whilst reading packet " + packetType);
            } else {
                objects.add(packet);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(ClientConnection.PACKET_RECEIVED_MARKER, " IN: [{}:{}] {} -> {} bytes", this.state.id().getId(), packetType, packet.getClass().getName(), i);
                }

                NetworkStateTransitionHandler.onDecoded(context, packet);
            }
        }
        ci.cancel();
    }
}
