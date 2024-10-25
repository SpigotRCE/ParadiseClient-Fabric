package io.github.spigotrce.paradiseclientfabric.mixin.inject.api;

import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import net.fabricmc.fabric.impl.networking.PayloadTypeRegistryImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PayloadTypeRegistryImpl.class)
public abstract class PayloadTypeRegistryImplMixin <B extends PacketByteBuf> {
    @Inject(method = "register", at = @At(value = "RETURN"))
    public <T extends CustomPayload> void register(CustomPayload.Id<T> id, PacketCodec<? super B, T> codec, CallbackInfoReturnable<CustomPayload.Type<? super B, T>> cir) {
//        ParadiseClient_Fabric.getNetworkMod().registeredChannelsByName.add(id.id().toString());
    }
}
