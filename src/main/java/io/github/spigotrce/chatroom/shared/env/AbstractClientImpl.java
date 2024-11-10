package io.github.spigotrce.chatroom.shared.env;

import io.github.spigotrce.chatroom.shared.network.connection.AbstractClientConnection;

public abstract class AbstractClientImpl extends AbstractEnvironment {
    public Class<? extends AbstractClientConnection> connection;
}
