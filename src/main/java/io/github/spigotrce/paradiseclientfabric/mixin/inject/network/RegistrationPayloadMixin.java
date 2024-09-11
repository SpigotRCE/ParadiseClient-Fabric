package io.github.spigotrce.paradiseclientfabric.mixin.inject.network;

import io.github.spigotrce.eventbus.event.listener.Listener;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import net.fabricmc.fabric.impl.networking.RegistrationPayload;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RegistrationPayload.class)
public class RegistrationPayloadMixin implements Listener {
    @Inject(method = "addId", at = @At(value = "HEAD"), remap = false)
    private static void addId(List<Identifier> ids, StringBuilder sb, CallbackInfo ci) {
        String channel = sb.toString();
        if (!channel.isEmpty()) {
            ParadiseClient_Fabric.getMiscMod().delayedMessages.add("§9Channel: §a" + channel);
        }
    }
}
