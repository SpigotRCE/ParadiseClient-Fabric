package io.github.spigotrce.chatroom.server;

import java.net.Socket;

/**
 * <p>A shard, handles a connection.</p>
 */
public class ServerShard implements Runnable {

    private Socket client;

    public ServerShard(ChatroomServer server, Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {

    }
}
