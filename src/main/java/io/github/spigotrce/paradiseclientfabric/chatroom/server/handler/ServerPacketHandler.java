package io.github.spigotrce.paradiseclientfabric.chatroom.server.handler;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.Version;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.PacketRegistry;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl.*;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Logging;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.Main;
import io.github.spigotrce.paradiseclientfabric.chatroom.server.netty.ChatRoomServer;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.UUID;

public class ServerPacketHandler extends AbstractPacketHandler {
    public UserModel userModel;
    public boolean isAuthenticated = false;
    public long lastMessage = 0;
    public long timeSinceKeepAlive = 0;
    public int keepAliveViolation = 0;

    public ServerPacketHandler(Channel channel) {
        super(channel);
    }

    @Override
    public void handle(HandshakePacket packet) throws Exception {
        if (isAuthenticated)
            throw new IllegalStateException("User already authenticated");
        if (packet.getPvn() != Version.PROTOCOL_VERSION) {
            PacketRegistry.sendPacket(
                    new HandshakeResponsePacket(new UserModel().withUsername("Invalid client version!"), false),
                    channel
            );
            channel.close();
            return;
        }

        String hostname = ((InetSocketAddress) channel.attr(AttributeKey.valueOf("proxiedAddress")).get()).getHostName();
        if (ChatRoomServer.lastConnectionTime.containsKey(hostname)) {
            if (ChatRoomServer.lastConnectionTime.get(hostname) + Main.CONFIG.getServer().connectionThrottle() > System.currentTimeMillis()) {
                PacketRegistry.sendPacket(
                        new HandshakeResponsePacket(
                                new UserModel().withUsername("Connection throttled!"),
                                false
                        ),
                        channel
                );
                channel.close();
                return;
            }
        }

        ChatRoomServer.lastConnectionTime.put(hostname, System.currentTimeMillis());

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

        new Thread(() -> {
            while (channel.isOpen()) {
                try {
                    Thread.sleep(5000);
                    if (timeSinceKeepAlive + 5000 < System.currentTimeMillis()) {
                        keepAliveViolation++;
                        if (keepAliveViolation > 3) {
                            Logging.info(userModel.username() + " Timed out!");
                            channel.close();
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    Logging.error("Error occurred while waiting.", e);
                    channel.close();
                }
            }
        }).start();
    }

    @Override
    public void handle(KeepAlivePacket packet) throws Exception {
        if (!isAuthenticated)
            throw new IllegalStateException("User not authenticated");
        // relay
        PacketRegistry.sendPacket(packet, channel);

        timeSinceKeepAlive = System.currentTimeMillis();
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
