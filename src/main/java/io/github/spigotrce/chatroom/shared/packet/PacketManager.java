package io.github.spigotrce.chatroom.shared.packet;

import io.github.spigotrce.chatroom.server.ConnectionHandler;
import io.github.spigotrce.chatroom.shared.ConnectionPhase;
import io.github.spigotrce.chatroom.shared.packet.impl.common.DisconnectPacket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class PacketManager {

    public final PacketDirection direction;
    protected Socket socketConnection;
    protected PrintWriter out;
    protected ConnectionHandler handler;
    public ConnectionPhase phase = ConnectionPhase.LOGIN;

    public PacketManager(PacketDirection direction, Socket socketConnection, PrintWriter out, ConnectionHandler handler) {
        this.direction = direction;
        this.socketConnection = socketConnection;
        this.out = out;
        this.handler = handler;
    }

    public void sendPacket(Packet packet) {
        this.out.println(packet.getPacketId() + "::" + packet.writer(this.direction));
    }

    public void handlePacket(String raw) {
        String[] packetSplit = raw.split("::", 2);
        String packetId = packetSplit[0];
        String packetData = packetSplit[1];

        Packet packet = PacketType.values()[Integer.parseInt(packetId)].create(this);

        packet.reader(this.direction, packetData);

        System.out.println("DEBUG: Packet Received -> " + packetId + " with data: " + packetData);
        this.onPacket(packet);
    }


    // To close the connection on the socket level meanwhile the disconnection packet is sent from the overrode method
    public boolean closeConnection() {
        return closeConnection("");
    }

    public boolean closeConnection(String message) {
        try {
            this.socketConnection.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public abstract void onPacket(Packet packet);
}