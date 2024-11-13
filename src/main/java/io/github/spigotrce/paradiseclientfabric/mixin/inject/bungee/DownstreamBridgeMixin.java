package io.github.spigotrce.paradiseclientfabric.mixin.inject.bungee;

import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.protocol.packet.PluginMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DownstreamBridge.class)
public class DownstreamBridgeMixin {
    @Inject(method = "handle(Lnet/md_5/bungee/protocol/packet/PluginMessage;)V", at = @At("HEAD"), remap = false, cancellable = true)
    public void handle(PluginMessage pluginMessage, CallbackInfo ci) {
        System.out.println(new String(pluginMessage.getData()));
        System.out.println("Data in channel");
        ci.cancel(); // we don't want to modify any data and let's just forward it to the client
    }
}
