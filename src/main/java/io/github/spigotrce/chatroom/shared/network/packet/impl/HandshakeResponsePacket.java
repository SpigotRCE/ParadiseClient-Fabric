package io.github.spigotrce.chatroom.shared.network.packet.impl;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.spigotrce.chatroom.shared.network.packet.Packet;

public class HandshakeResponsePacket extends Packet {
    private boolean success;

    public HandshakeResponsePacket(boolean username) {
        super("handshake_response");
        this.success = username;
    }

    public HandshakeResponsePacket(byte[] buf) {
        super("handshake_response");
        this.buf = buf;
    }

    public HandshakeResponsePacket() {
        super("handshake_response");
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public void encode() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeBoolean(success);
        buf = out.toByteArray();
    }

    @Override
    public void decode() {
        ByteArrayDataInput in = ByteStreams.newDataInput(buf);
        success = in.readBoolean();
    }

    public HandshakeResponsePacket createPacket() {
        return new HandshakeResponsePacket();
    }
}
