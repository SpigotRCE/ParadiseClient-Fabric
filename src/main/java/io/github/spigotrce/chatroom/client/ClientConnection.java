package io.github.spigotrce.chatroom.client;

import io.github.spigotrce.chatroom.shared.network.connection.AbstractClientConnection;
import io.github.spigotrce.chatroom.shared.network.packet.Packet;

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
    public void sendPacket(Class<? extends Packet> packet) {

    }

    @Override
    public void receivePacket(byte[] data) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
