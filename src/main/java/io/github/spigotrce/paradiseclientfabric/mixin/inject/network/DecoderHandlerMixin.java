package io.github.spigotrce.paradiseclientfabric.mixin.inject.network;

import com.mojang.logging.LogUtils;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.channel.PluginMessageEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.handler.DecoderHandler;
import net.minecraft.network.listener.PacketListener;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DecoderHandler.class)
public class DecoderHandlerMixin <T extends PacketListener>{
    @Shadow private static final Logger LOGGER = LogUtils.getLogger();
    @Mutable
    @Final
    @Shadow private final NetworkState<T> state;

    public DecoderHandlerMixin(NetworkState<T> state) {
        this.state = state;
    }

    @Inject(method = "decode", at = @At("HEAD"), cancellable = true)
    public void decode(ChannelHandlerContext context, ByteBuf b, List<Object> objects, CallbackInfo ci) {
        PacketByteBuf buf = new PacketByteBuf(b.copy());
        if (buf.readVarInt() != 25) return;
        PluginMessageEvent event = new PluginMessageEvent(buf.readString(), buf);
        try {
            ParadiseClient_Fabric.getEventManager().fireEvent(event);
        } catch (Exception e) {
            LOGGER.error("Unable to fire PluginMessageEvent", e);
            LOGGER.error("Not dropping the packet! (TODO: Change this in the future)");
            return;
        }
        if (event.isCancel()) ci.cancel();
    }
}
