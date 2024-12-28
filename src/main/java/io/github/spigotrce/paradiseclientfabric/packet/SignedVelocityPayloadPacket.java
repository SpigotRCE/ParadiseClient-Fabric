package io.github.spigotrce.paradiseclientfabric.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SignedVelocityPayloadPacket(String uuid, String command) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, SignedVelocityPayloadPacket> CODEC = CustomPayload.codecOf(SignedVelocityPayloadPacket::write, SignedVelocityPayloadPacket::new);
    public static final Id<SignedVelocityPayloadPacket> ID = new Id<>(Identifier.of("signedvelocity", "main"));

    private SignedVelocityPayloadPacket(PacketByteBuf buf) {
        this(buf.readString(), buf.readString());
    }

    public SignedVelocityPayloadPacket(String uuid, String command) {
        this.uuid = uuid;
        this.command = command;
    }

    private void write(PacketByteBuf buf) {
        buf.writeByte(0);
        buf.writeString(uuid);
        buf.writeByte(0);
        buf.writeString("COMMAND_RESULT");
        buf.writeByte(0);
        buf.writeString("MODIFY");
        buf.writeByte(0);
        buf.writeString("/" + command);
    }

    public Id<SignedVelocityPayloadPacket> getId() {
        return ID;
    }
}
