package io.github.spigotrce.chatroom.server;

import io.github.spigotrce.chatroom.shared.PacketType;
import io.github.spigotrce.chatroomold.server.ServerPacketManager;
import io.github.spigotrce.chatroomold.shared.packet.impl.common.DisconnectPacket;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <p>A shard, handles a connection.</p>
 */
public class ServerShard implements Runnable {

    private ChatroomServer server;

    private BufferedReader in;
    private PrintWriter out;

    private Socket client;

    public ServerShard(ChatroomServer server, Socket socket) {
        this.client = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            this.in = new BufferedReader(new java.io.InputStreamReader(client.getInputStream()));
            this.out = new PrintWriter(client.getOutputStream(), true);
            String packet;
            while ((packet = in.readLine()) != null){
                String[] cut = packet.split(";");
                PacketType type = this.server.registry.getLeft(Integer.parseInt(cut[0]));

                this.server.registry.getRight(type).packetHandler.handle(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.server.shards.remove(this);
        }
    }


}
