package io.github.spigotrce.paradiseclientfabric.mixin.inject.etc;

import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        ParadiseClient_Fabric.getMiscMod().delayedMessages.forEach(this::sendMessage);
        ParadiseClient_Fabric.getMiscMod().delayedMessages.clear();
    }

    @Unique
    private void sendMessage(Text message) {
        this.sendMessage(message, false);
    }
}
