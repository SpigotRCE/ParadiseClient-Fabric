package io.github.spigotrce.chatroom.client;

import io.github.spigotrce.chatroom.shared.env.AbstractClientImpl;
import io.github.spigotrce.chatroom.shared.network.NetworkState;
import io.github.spigotrce.chatroom.shared.network.connection.AbstractClientConnection;
import io.github.spigotrce.chatroom.shared.network.connection.AbstractServerConnection;
import io.github.spigotrce.chatroom.shared.network.packet.impl.HandshakePacket;

public class ClientImpl extends AbstractClientImpl {
    @Override
    public void onConnect(AbstractClientConnection connection) {
        connection.state = NetworkState.HANDSHAKE;
        connection.sendPacket(new HandshakePacket("Username", "Password", "gb6"));
    }

    @Override
    public void onLoginSuccess(AbstractServerConnection connection) {
        connection.state = NetworkState.PLAY;
    }
}
