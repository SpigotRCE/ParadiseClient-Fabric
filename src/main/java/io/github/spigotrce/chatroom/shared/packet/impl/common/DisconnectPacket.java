package io.github.spigotrce.chatroom.shared.packet.impl.common;

import io.github.spigotrce.chatroom.shared.ProtocolUtil;
import io.github.spigotrce.chatroom.shared.packet.PacketDirection;
import io.github.spigotrce.chatroom.shared.packet.Packet;
import io.github.spigotrce.chatroom.shared.packet.PacketManager;
import io.github.spigotrce.chatroom.shared.packet.PacketType;

public class DisconnectPacket extends Packet {

    private String message;

    public DisconnectPacket(PacketManager manager) {
        super(PacketType.DISCONNECT, manager);
    }

    public DisconnectPacket(PacketManager manager, String message) {
        super(PacketType.DISCONNECT, manager);
        this.message = message;
    }

    @Override
    public void reader(PacketDirection direction, String raw) {
        if (direction == PacketDirection.CLIENT) message = ProtocolUtil.read(raw)[0];
    }

    @Override
    public String writer(PacketDirection direction) {
        if (direction == PacketDirection.SERVER) return ProtocolUtil.write(message);
        return "";
    }
}
