package io.github.spigotrce.paradiseclientfabric.mixin.inject.api;

import io.github.spigotrce.paradiseclientfabric.mixin.accessor.PayloadTypeRegistryImplAccessor;
import net.fabricmc.fabric.impl.networking.PayloadTypeRegistryImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(PayloadTypeRegistryImpl.class)
public abstract class PayloadTypeRegistryImplMixin<B extends PacketByteBuf> implements PayloadTypeRegistryImplAccessor {
    @Shadow(remap = false)
    @Final
    private Map<Identifier, CustomPayload.Type<B, ? extends CustomPayload>> packetTypes;

    @Inject(method = "register", at = @At(value = "RETURN"))
    public <T extends CustomPayload> void register(CustomPayload.Id<T> id, PacketCodec<? super B, T> codec, CallbackInfoReturnable<CustomPayload.Type<? super B, T>> cir) {
    }

    @Override
    public ArrayList<String> paradiseClient_Fabric$getRegisteredChannelsByName() {
        return packetTypes.keySet().stream()
                .map(Identifier::toString)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
