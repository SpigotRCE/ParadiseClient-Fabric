package io.github.spigotrce.paradiseclientfabric.mixin.inject.network;

import com.mojang.logging.LogUtils;
import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.Helper;
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

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

@Mixin(DecoderHandler.class)
public class DecoderHandlerMixin <T extends PacketListener>{
    @Shadow private static final Logger LOGGER = LogUtils.getLogger();
    @Mutable
    @Final
    @Shadow private final NetworkState<T> state;

    public DecoderHandlerMixin(NetworkState<T> state) {
        this.state = state;
    }

    @Inject(method = "decode", at = @At("HEAD"))
    public void decode(ChannelHandlerContext context, ByteBuf b, List<Object> objects, CallbackInfo ci) {
        PacketByteBuf buf = new PacketByteBuf(b.copy());
        if (buf.readVarInt() != 25) return;
        processPayload(buf.readString(), buf);
    }

    @Unique
    private void processPayload(String channelName, PacketByteBuf buf) {
        try {
            if (Objects.equals(channelName, "minecraft:register") || Objects.equals(channelName, "REGISTER")) // 1.13 channel or 1.8 channel
                for(String splitted : buf.toString(Charset.defaultCharset()).split("\000")) {
                    Helper.printChatMessage("&fChannel: &d" + splitted);
                }
            else
                Helper.printChatMessage("&fChannel: &d" + channelName + " &fData: &d" + buf.toString(Charset.defaultCharset()));
        } catch (Exception e) {
            Helper.printChatMessage("&4Error processing payload for channel: " + channelName + " " + e.getMessage());
            Constants.LOGGER.error("&4Error processing payload for channel: {} {}", channelName, e);
        }
    }
}
