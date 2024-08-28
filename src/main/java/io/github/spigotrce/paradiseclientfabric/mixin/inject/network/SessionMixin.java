package io.github.spigotrce.paradiseclientfabric.mixin.inject.network;

import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import io.github.spigotrce.paradiseclientfabric.mixin.accessor.SessionAccessor;

@Mixin(Session.class)
public class SessionMixin implements SessionAccessor {
    @Final
    @Shadow
    @Mutable
    private String username;

    @Override
    public void paradiseClient_Fabric$setUsername(String username) {
        this.username = username;
    }
}