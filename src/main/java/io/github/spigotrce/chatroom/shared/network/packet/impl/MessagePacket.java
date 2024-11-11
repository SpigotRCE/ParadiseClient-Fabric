package io.github.spigotrce.chatroom.shared.network.packet.impl;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.spigotrce.chatroom.shared.network.packet.Packet;

public class MessagePacket extends Packet {
    private String message;
    public MessagePacket(String message) {
        super("message");
        this.message = message;
    }

    public MessagePacket(byte[] buf) {
        super("message");
        this.buf = buf;
    }

    public MessagePacket() {
        super("message");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void encode() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(message);
        buf = out.toByteArray();
    }

    @Override
    public void decode() {
        message = ByteStreams.newDataInput(buf).readUTF();
    }

    public MessagePacket createPacket() {
        return new MessagePacket();
    }
}
