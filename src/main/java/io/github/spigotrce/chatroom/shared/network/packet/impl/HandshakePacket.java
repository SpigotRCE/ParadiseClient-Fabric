package io.github.spigotrce.chatroom.shared.network.packet.impl;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.spigotrce.chatroom.shared.network.packet.Packet;

public class HandshakePacket extends Packet {
    private String username;
    private String password;
    private String version;

    public HandshakePacket(String username, String password, String version) {
        super("handshake");
        this.username = username;
        this.password = password;
        this.version = version;
    }

    public HandshakePacket() {
        super("handshake");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public void encode() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(username);
        out.writeUTF(password);
        out.writeUTF(version);
        buf = out.toByteArray();
    }

    @Override
    public void decode() {
        ByteArrayDataInput in = ByteStreams.newDataInput(buf);
        in.readUTF();
        in.readUTF();
        in.readUTF();
    }

    public HandshakePacket createPacket() {
        return new HandshakePacket();
    }
}
