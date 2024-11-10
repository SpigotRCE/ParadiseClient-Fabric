package io.github.spigotrce.chatroom.shared.env;

import io.github.spigotrce.chatroom.shared.network.connection.AbstractClientConnection;

import java.net.ServerSocket;
import java.util.ArrayList;

public abstract class AbstractServerImpl extends AbstractEnvironment {
    public boolean isRunning;
    public ServerSocket serverSocket;
    public final ArrayList<Class<? extends AbstractClientConnection>> connections = new ArrayList<>();
}
