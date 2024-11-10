package io.github.spigotrce.chatroom.server;

import io.github.spigotrce.chatroom.shared.network.connection.AbstractServerConnection;
import io.github.spigotrce.chatroom.shared.network.packet.Packet;

public class ServerConnection extends AbstractServerConnection {
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
    public void receiveData(byte[] data) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
