package io.github.spigotrce.chatroom.shared.network.server;

import io.github.spigotrce.chatroom.shared.network.connection.AbstractClientConnection;

import java.net.ServerSocket;
import java.util.ArrayList;

public abstract class AbstractServerImpl implements Runnable {
    public ServerSocket serverSocket;
    public boolean isRunning;
    public final ArrayList<Class<? extends AbstractClientConnection>> connections = new ArrayList<>();
}
