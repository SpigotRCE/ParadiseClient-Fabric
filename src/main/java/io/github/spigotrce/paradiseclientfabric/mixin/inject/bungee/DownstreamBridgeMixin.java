package io.github.spigotrce.paradiseclientfabric.mixin.inject.bungee;

import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import net.md_5.bungee.connection.CancelSendSignal;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.protocol.packet.PluginMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DownstreamBridge.class, remap = false)
public class DownstreamBridgeMixin {
    @Inject(method = "handle(Lnet/md_5/bungee/protocol/packet/PluginMessage;)V", at = @At("HEAD"), remap = false, cancellable = true)
    public void handle(PluginMessage pluginMessage, CallbackInfo ci) {
        byte[] b = pluginMessage.getData();
        io.github.spigotrce.paradiseclientfabric.event.channel.PluginMessageEvent event = new io.github.spigotrce.paradiseclientfabric.event.channel.PluginMessageEvent(pluginMessage.getTag(), b);
        try {
            ParadiseClient_Fabric.getEventManager().fireEvent(event);
        } catch (Exception e) {
            Constants.LOGGER.error("Unable to fire PluginMessageEvent", e);
            Constants.LOGGER.error("Not dropping the packet! (TODO: Change this in the future)");
            return;
        }

        if (event.isCancel()) throw CancelSendSignal.INSTANCE;
        ci.cancel(); // we don't want to modify any data and let's just forward it to the client
    }
}
