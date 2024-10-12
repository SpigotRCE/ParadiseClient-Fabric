package io.github.spigotrce.chatroom.server;

import io.github.spigotrce.chatroom.shared.ConnectionPhase;
import io.github.spigotrce.chatroom.shared.packet.Packet;
import io.github.spigotrce.chatroom.shared.packet.impl.common.DisconnectPacket;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private final Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ServerPacketManager packetManager;
    private ServerImpl server;
    private String clientName;

    /**
     * Constructs a connection handler.
     *
     * @param client The client.
     */
    public ConnectionHandler(Socket client, ServerImpl server) {
        this.client = client;
        this.server = server;
    }

    /**
     * Runs the connection handler.
     */
    @Override
    public void run() {
        try {
            in = new BufferedReader(new java.io.InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            this.packetManager = new ServerPacketManager(this.out, this.client, this);
            String packet;
            while ((packet = in.readLine()) != null)
                this.packetManager.handlePacket(packet);
        } catch (Exception e) {
            e.printStackTrace();
            this.server.getConnections().remove(this);
            this.packetManager.sendPacket(new DisconnectPacket(getPacketManager(), "Exception reading data."));
        }
    }

    public void sendMessage(String message) {
        // unimplemented
    }

    public void sendPacket(Packet packet) {
        this.packetManager.sendPacket(packet);
    }

    public ServerPacketManager getPacketManager() {
        return this.packetManager;
    }
}
