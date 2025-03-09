package io.github.spigotrce.paradiseclientfabric.chatroom.client.handler;

import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.PacketRegistry;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakeResponsePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.KeepAlivePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Logging;
import io.github.spigotrce.paradiseclientfabric.mod.ChatRoomMod;
import io.netty.channel.Channel;

public class ClientPacketHandler extends AbstractPacketHandler {
    private boolean isAuthenticated = false;
    private UserModel userModel;
    public long timeSinceKeepAlive = 0;
    public int keepAliveViolation = 0;

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
            Helper.printChatMessage("[ChatRoom] Failed to authenticate: " + userModel.username());
        ParadiseClient_Fabric.CHAT_ROOM_MOD.user = userModel;

        new Thread(() -> {
            while (channel.isOpen()) {
                try {
                    Thread.sleep(5000);
                    if (timeSinceKeepAlive + 5000 < System.currentTimeMillis()) {
                        keepAliveViolation++;
                        if (keepAliveViolation > 3) {
                            Helper.printChatMessage("[ChatRoom] Timed out");
                            channel.close();
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    Constants.LOGGER.error("Error occurred while waiting.", e);
                    Helper.printChatMessage("[ChatRoom] Error occurred while waiting");
                    channel.close();
                }
            }
        }).start();

        new Thread(() -> {
            while (channel.isOpen()) {
                try {
                    Thread.sleep(5000);
                    PacketRegistry.sendPacket(new KeepAlivePacket(1), channel);
                    Helper.printChatMessage("Sending packet!");
                } catch (InterruptedException e) {
                    Constants.LOGGER.error("Error occurred while waiting.", e);
                    channel.close();
                }
            }
        }).start();
    }

    @Override
    public void handle(KeepAlivePacket packet) throws Exception {
        if (!isAuthenticated)
            throw new IllegalStateException("User not authenticated");
        timeSinceKeepAlive = System.currentTimeMillis();
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
