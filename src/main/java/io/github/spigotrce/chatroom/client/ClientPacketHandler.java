package io.github.spigotrce.chatroom.client;

import io.github.spigotrce.chatroom.shared.network.exception.IllegalPacketDirectionException;
import io.github.spigotrce.chatroom.shared.network.packet.AbstractPacketHandler;
import io.github.spigotrce.chatroom.shared.network.packet.impl.DisconnectPacket;
import io.github.spigotrce.chatroom.shared.network.packet.impl.HandshakePacket;
import io.github.spigotrce.chatroom.shared.network.packet.impl.MessagePacket;

public class ClientPacketHandler extends AbstractPacketHandler {
    @Override
    public void handle(DisconnectPacket packet) throws Exception {

    }

    @Override
    public void handle(HandshakePacket packet) throws Exception {
        throw new IllegalPacketDirectionException("Handshake packet is only c2s and not s2c");
    }

    @Override
    public void handle(MessagePacket packet) throws Exception {

    }
}
