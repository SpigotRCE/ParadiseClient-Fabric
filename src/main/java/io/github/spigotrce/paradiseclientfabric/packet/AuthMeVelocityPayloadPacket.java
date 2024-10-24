package io.github.spigotrce.paradiseclientfabric.packet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record AuthMeVelocityPayloadPacket() implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, AuthMeVelocityPayloadPacket> CODEC = CustomPayload.codecOf(AuthMeVelocityPayloadPacket::write, AuthMeVelocityPayloadPacket::new);
    public static final Id<AuthMeVelocityPayloadPacket> ID = new Id<>(Identifier.of("authmevelocity", "main"));

    private AuthMeVelocityPayloadPacket(PacketByteBuf buf) {
        this();
    }

    private void write(PacketByteBuf buf) {
        buf.writeByte(0);
        buf.writeString("LOGIN");
        buf.writeByte(0);
        buf.writeString(MinecraftClient.getInstance().getGameProfile().getName());
    }

    public CustomPayload.Id<AuthMeVelocityPayloadPacket> getId() {
        return ID;
    }
}
