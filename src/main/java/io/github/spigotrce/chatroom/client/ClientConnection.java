package io.github.spigotrce.chatroom.client;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.spigotrce.chatroom.shared.network.connection.AbstractClientConnection;
import io.github.spigotrce.chatroom.shared.network.packet.Packet;
import io.github.spigotrce.chatroom.shared.network.packet.PacketFactory;
import io.github.spigotrce.chatroom.shared.network.packet.impl.HandshakePacket;

import java.nio.charset.Charset;

public class ClientConnection extends AbstractClientConnection {
    @Override
    public void setUsername(String username) {

    }

    @Override
    public void setVersion(String version) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void message(String message) {

    }

    @Override
    public void sendPacket(Packet packet) {
        packet.encode();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(packet.getId());
        out.writeUTF(new String(packet.buf, Charset.defaultCharset()));
        out.toByteArray(); // Send this
    }

    @Override
    public void receiveData(byte[] data) {
        ByteArrayDataInput in = ByteStreams.newDataInput(data);
        PacketFactory.decodeAndApply(in.readUTF(), in.readUTF().getBytes(Charset.defaultCharset()));
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
