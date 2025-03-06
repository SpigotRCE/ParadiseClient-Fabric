package io.github.spigotrce.paradiseclientfabric.chatroom.server.handler;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.PacketRegistry;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.DisconnectPacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.HandshakeResponsePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.MessagePacket;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Logging;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Main;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.netty.ChatRoomServer;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.Arrays;
import java.util.UUID;

public class ServerPacketHandler extends AbstractPacketHandler {
    public UserModel userModel;
    public boolean isAuthenticated = false;
    public long lastMessage = 0;

    public ServerPacketHandler(Channel channel) {
        super(channel);
    }

    @Override
    public void handle(HandshakePacket packet) throws Exception {
        if (isAuthenticated)
            throw new IllegalStateException("User already authenticated");

        UUID uuid = UUID.fromString(Arrays.asList(packet.getToken().split("\\.")).getFirst());

        isAuthenticated = Main.authenticate(packet.getToken());
        if (!isAuthenticated) {
            PacketRegistry.sendPacket(
                    new HandshakeResponsePacket(new UserModel(), false),
                    channel
            );
            channel.close();
            return;
        }


        userModel = Main.DATABASE.getUser(uuid);
        if (ChatRoomServer.onlineUsers.contains(userModel)) {
            PacketRegistry.sendPacket(
                    new HandshakeResponsePacket(new UserModel().withUsername("You are already connected the server."), false),
                    channel
            );
            userModel = null;
            channel.close();
            return;
        }
        PacketRegistry.sendPacket(
                new HandshakeResponsePacket(userModel, true),
                channel
        );
        Logging.info("Connection: " + userModel.username() + channel.attr(AttributeKey.valueOf("proxiedAddress")).get());
        ChatRoomServer.onlineUsers.add(userModel);

        ChatRoomServer.broadcastMessage(userModel.username() + " joined the chat");

    }

    @Override
    public void handle(DisconnectPacket packet) throws Exception {
        channel.close();
    }

    @Override
    public void handle(MessagePacket packet) throws Exception {
        if (!isAuthenticated)
            throw new IllegalStateException("User not authenticated");

        if (lastMessage + Main.CONFIG.getServer().messageCooldown() > System.currentTimeMillis()) {
            PacketRegistry.sendPacket(
                    new MessagePacket("Do not spam messages!"),
                    channel
            );
            return;
        }
        lastMessage = System.currentTimeMillis();
        if (packet.getMessage().length() > Main.CONFIG.getServer().maxMessageCharacters()) {
            PacketRegistry.sendPacket(
                    new MessagePacket("Message is too long!"),
                    channel
            );
            return;
        }

        ChatRoomServer.broadcastMessage(
                userModel.username() +
                        ">>" +
                        packet.getMessage()
        );
    }
}
