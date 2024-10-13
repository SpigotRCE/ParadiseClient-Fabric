package io.github.spigotrce.chatroom.server;

import io.github.spigotrce.chatroom.server.handlers.ServerHandlers;
import io.github.spigotrce.chatroom.shared.PacketProcessor;
import io.github.spigotrce.chatroomold.server.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatroomServer extends PacketProcessor<ServerHandlers> implements Runnable {

    private ServerSocket serverSocket;
    private boolean done;

    public List<ServerShard> shards = new ArrayList<>();

    /**
     * <p>Creates a new packet processor.</p>
     *
     */
    public ChatroomServer() {
        super(ServerHandlers.values());
    }

    @Override
    public void run() {
        try {
            System.out.println("[INFO] Starting classroom server!");
            this.serverSocket = new ServerSocket(58538);
            ExecutorService pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = this.serverSocket.accept();
                ServerShard serverShard = new ServerShard(this, client);
                this.shards.add(serverShard);
                pool.execute(serverShard);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown() {
        System.out.println("Shutting down server");
        done = true;
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (ServerShard serverShard : this.shards) {
            //todo: spread
        }
    }
}
