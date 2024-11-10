package io.github.spigotrce.chatroom.server;

import io.github.spigotrce.chatroom.shared.env.AbstractServerImpl;
import io.github.spigotrce.chatroom.shared.network.connection.AbstractServerConnection;

public class ServerImpl extends AbstractServerImpl {
    @Override
    public void onNewConnection(Class<? extends AbstractServerConnection> connection) {

    }
}
