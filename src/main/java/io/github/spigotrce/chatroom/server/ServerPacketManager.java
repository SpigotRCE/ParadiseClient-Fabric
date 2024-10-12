package io.github.spigotrce.chatroom.server;

import io.github.spigotrce.chatroom.shared.packet.Packet;
import io.github.spigotrce.chatroom.shared.packet.PacketDirection;
import io.github.spigotrce.chatroom.shared.packet.PacketManager;
import io.github.spigotrce.chatroom.shared.packet.PacketType;
import io.github.spigotrce.chatroom.shared.packet.impl.common.DisconnectPacket;

import java.io.PrintWriter;
import java.net.Socket;

// Per client instance
public class ServerPacketManager extends PacketManager {

    public ServerPacketManager(PrintWriter out, Socket client, ConnectionHandler connectionHandler) {
        super(PacketDirection.SERVER, client, out, connectionHandler);
    }

    @Override
    public boolean closeConnection() {
        this.sendPacket(new DisconnectPacket(this));
        return super.closeConnection();
    }

    @Override
    public boolean closeConnection(String message) {
        this.sendPacket(new DisconnectPacket(this, message));
        return super.closeConnection(message);
    }

    @Override
    public void onPacket(Packet packet) {
        if (packet.getType() == PacketType.DISCONNECT) {
            super.closeConnection();
            System.out.println("Client disconnected");
            return;
        }
        System.out.println("Unhandled packet type: " + packet.getType());
    }
}