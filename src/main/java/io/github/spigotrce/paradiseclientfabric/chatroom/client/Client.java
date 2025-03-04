package io.github.spigotrce.paradiseclientfabric.chatroom.client;

import io.github.spigotrce.paradiseclientfabric.chatroom.client.netty.ChatRoomClient;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.PacketRegistry;

public class Client {
    public static void main(String[] args) {
        PacketRegistry.registerPackets();
        try {
            new ChatRoomClient("localhost", 45000).connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
