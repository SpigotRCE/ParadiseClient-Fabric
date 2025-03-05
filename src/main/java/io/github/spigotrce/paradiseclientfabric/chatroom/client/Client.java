package io.github.spigotrce.paradiseclientfabric.chatroom.client;

import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.chatroom.client.netty.ChatRoomClient;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.PacketRegistry;

public class Client {
    public static void connected() throws Exception {
        PacketRegistry.registerPackets();
        new ChatRoomClient("localhost", 45000).connect();
        ParadiseClient_Fabric.CHAT_ROOM_MOD.isConnected = false;
    }
}
