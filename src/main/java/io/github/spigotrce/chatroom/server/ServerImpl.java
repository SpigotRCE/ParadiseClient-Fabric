package io.github.spigotrce.chatroom.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerImpl implements Runnable {
    private static ServerImpl serverImpl;
    private final ArrayList<ConnectionHandler> connections = new ArrayList<>();
    ServerSocket serverSocket;
    private boolean done = false;

    public static ServerImpl getServer() {
        return serverImpl;
    }

    @Override
    public void run() {
        serverImpl = this;
        try {
            System.out.println("Starting server at 0.0.0.0:58538");
            serverSocket = new ServerSocket(58538);
            ExecutorService pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = serverSocket.accept();
                System.out.println("Connection from " + client.getInetAddress().getHostAddress());
                ConnectionHandler connectionHandler = new ConnectionHandler(client, getServer());
                connections.add(connectionHandler);
                pool.execute(connectionHandler);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void broadcast(String message) {
        for (ConnectionHandler ch : connections)
            if (ch != null)
                ch.sendMessage(message);
    }

    public void shutdown() {
        System.out.println("Server is shutting down...");
        done = true;
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (ConnectionHandler ch : connections) {
            if (ch != null) {
                if (ch.getPacketManager().closeConnection("Server is shutting down...")) {
                    connections.remove(ch);
                }
            }
        }
    }

    public ArrayList<ConnectionHandler> getConnections() {
        return this.connections;
    }
}
