package io.github.spigotrce.chatroom.shared.packet.impl.common;

import io.github.spigotrce.chatroom.shared.packet.ImplDirection;
import io.github.spigotrce.chatroom.shared.packet.Packet;

public class DisconnectPacket extends Packet {
    private String reason;

    public DisconnectPacket(ImplDirection implDirection) {
        super(implDirection, "disconnect");
        this.reason = "";
    }

    public DisconnectPacket(String reason, ImplDirection implDirection) {
        this(implDirection);
        this.reason = reason;
    }

    @Override
    public String encode() {
        return "";
    }

    @Override
    public String[] decode(String input) {
        return new String[0];
    }
}
