package io.github.spigotrce.chatroom.shared.packet.impl.client;

import io.github.spigotrce.chatroom.shared.ProtocolUtil;
import io.github.spigotrce.chatroom.shared.packet.ImplDirection;
import io.github.spigotrce.chatroom.shared.packet.Packet;

public class CommandPacket extends Packet {
    private String command;

    public CommandPacket(ImplDirection implDirection) {
        super(implDirection, "command");
        this.command = "";
    }

    public CommandPacket(String command) {
        this(ImplDirection.CLIENT);
        this.command = command;
    }

    @Override
    public String encode() {
        return ProtocolUtil.write(command);
    }

    @Override
    public String[] decode(String input) {
        return ProtocolUtil.read(input);
    }
}
