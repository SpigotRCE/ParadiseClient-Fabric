package io.github.spigotrce.chatroom.server;

import io.github.spigotrce.chatroom.shared.network.packet.AbstractPacketHandler;
import io.github.spigotrce.chatroom.shared.network.packet.impl.DisconnectPacket;
import io.github.spigotrce.chatroom.shared.network.packet.impl.HandshakePacket;
import io.github.spigotrce.chatroom.shared.network.packet.impl.HandshakeResponsePacket;
import io.github.spigotrce.chatroom.shared.network.packet.impl.MessagePacket;

public class ServerPacketHandler extends AbstractPacketHandler {
    @Override
    public void handle(DisconnectPacket packet) throws Exception {

    }

    @Override
    public void handle(HandshakePacket packet) throws Exception {

    }

    @Override
    public void handle(HandshakeResponsePacket packet) throws Exception {

    }

    @Override
    public void handle(MessagePacket packet) throws Exception {

    }
}
