package io.github.spigotrce.paradiseclientfabric.chatroom.client.handler;

import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakeResponsePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;
import io.github.spigotrce.paradiseclientfabric.mod.ChatRoomMod;
import io.netty.channel.Channel;

public class ClientPacketHandler extends AbstractPacketHandler {
    private boolean isAuthenticated = false;
    private UserModel userModel;

    public ClientPacketHandler(Channel channel) {
        super(channel);
    }

    @Override
    public void handle(HandshakeResponsePacket packet) {
        if (isAuthenticated) return;
        isAuthenticated = packet.isSuccess();
        userModel = packet.getUserModel();
        if (isAuthenticated)
            Helper.printChatMessage("[ChatRoom] Connected as " + userModel.username());
        else
            Helper.printChatMessage("[ChatRoom] Disconnected before login");
        ParadiseClient_Fabric.CHAT_ROOM_MOD.user = userModel;
    }

    @Override
    public void handle(DisconnectPacket packet) {
        Helper.printChatMessage("[ChatRoom] Disconnected from server: " + packet.getMessage());
        channel.close();
    }

    @Override
    public void handle(MessagePacket packet) {
        Helper.printChatMessage("[ChatRoom] " + packet.getMessage());
    }
}
