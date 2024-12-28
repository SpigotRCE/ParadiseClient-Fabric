package io.github.spigotrce.paradiseclientfabric.packet;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ECBPayloadPacket(String command) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, ECBPayloadPacket> CODEC = CustomPayload.codecOf(ECBPayloadPacket::write, ECBPayloadPacket::new);
    public static final Id<ECBPayloadPacket> ID = new Id<>(Identifier.of("ecb", "channel"));

    public ECBPayloadPacket(PacketByteBuf buf) {
        this(buf.readString());
    }

    public ECBPayloadPacket(String command) {
        this.command = command;
    }

    public void write(PacketByteBuf buf) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ActionsSubChannel");
        out.writeUTF("console_command: " + command);
        buf.writeBytes(out.toByteArray());
    }

    public Id<ECBPayloadPacket> getId() {
        return ID;
    }
}