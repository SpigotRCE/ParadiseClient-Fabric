package io.github.spigotrce.paradiseclientfabric.packet;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
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
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LOGIN");
        out.writeUTF(MinecraftClient.getInstance().getGameProfile().getName());
        buf.writeBytes(out.toByteArray());
    }

    public CustomPayload.Id<AuthMeVelocityPayloadPacket> getId() {
        return ID;
    }
}
