package io.github.spigotrce.chatroom.shared.network.packet.impl;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.spigotrce.chatroom.shared.network.packet.Packet;

public class DisconnectPacket extends Packet {
    private String reason;
    public DisconnectPacket(String reason) {
        super("disconnect");
        this.reason = reason;
    }

    public DisconnectPacket(byte[] data) {
        super("disconnect");
        reason = ByteStreams.newDataInput(data).readUTF();
    }

    public DisconnectPacket() {
        super("disconnect");
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void encode() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(reason);
        buf = out.toByteArray();
    }

    @Override
    public void decode() {
        reason = ByteStreams.newDataInput(buf).readUTF();
    }

    public DisconnectPacket createPacket() {
        return new DisconnectPacket();
    }
}
