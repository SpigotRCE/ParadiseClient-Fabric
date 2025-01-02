package io.github.spigotrce.paradiseclientfabric.packet;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record VelocityReportPayloadPacket(String s) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, VelocityReportPayloadPacket> CODEC = CustomPayload.codecOf(VelocityReportPayloadPacket::write, VelocityReportPayloadPacket::new);
    public static final CustomPayload.Id<VelocityReportPayloadPacket> ID = new Id<>(Identifier.of("velocityreport", "main"));

    private VelocityReportPayloadPacket(PacketByteBuf buf) {
        this(buf.readString());
    }

    public VelocityReportPayloadPacket(String s) {
        this.s = s;
    }

    private void write(PacketByteBuf buf) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(
                "{\"reporter\": \"{a}\", \"reported\":\"{b}\", \"server\":\"{c}\", \"reason\":\"{d}\", \"type\":\"NewReport\"}"
                        .replace("{a}", s)
                        .replace("{b}", s)
                        .replace("{c}", s)
                        .replace("{d}", s)
        );
        buf.writeBytes(out.toByteArray());
    }

    public Id<VelocityReportPayloadPacket> getId() {
        return ID;
    }
}
