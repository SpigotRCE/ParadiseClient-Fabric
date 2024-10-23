package io.github.spigotrce.paradiseclientfabric.mixin.inject.network;

import io.github.spigotrce.eventbus.event.listener.Listener;
import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.channel.ChannelRegisterEvent;
import net.fabricmc.fabric.impl.networking.RegistrationPayload;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RegistrationPayload.class)
public class RegistrationPayloadMixin implements Listener {
    @Inject(method = "addId", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void addId(List<Identifier> ids, StringBuilder sb, CallbackInfo ci) {
        String channel = sb.toString();
        if (channel.isEmpty()) return;

        ChannelRegisterEvent event = new ChannelRegisterEvent(sb.toString(), "§9Channel: §a" + channel);
        try {
            ParadiseClient_Fabric.getEventManager().fireEvent(event);
        } catch (Exception e) {
            Constants.LOGGER.error("Unable to fire ChannelRegisterEvent", e);
        }

        if (event.isCancel()) ci.cancel();
        Helper.printChatMessage(event.getMessage());
    }
}
