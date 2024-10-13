package io.github.spigotrce.chatroom.client;

import io.github.spigotrce.chatroom.server.ChatroomServer;
import io.github.spigotrce.chatroom.shared.PacketProcessor;
import io.github.spigotrce.chatroom.shared.PacketType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <p>A chatroom client.</p>
 */
public class ChatroomClient extends PacketProcessor {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * <p>Creates a chatroom client.</p>
     * @param ip the ip
     * @param port the port.
     * @throws Exception
     */
    public ChatroomClient(String ip, int port) throws Exception {
        super(null);
        this.socket = new Socket(ip, port);

        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        String packet;
        while ((packet = in.readLine()) != null) {
            String[] cut = packet.split(";");
            PacketType type = this.registry.getLeft(Integer.parseInt(cut[0]));

            this.registry.getRight(type).packetHandler.handle(this);
        }
    }

}
