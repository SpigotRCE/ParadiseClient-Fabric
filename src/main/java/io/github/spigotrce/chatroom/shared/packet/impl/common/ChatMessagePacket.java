package io.github.spigotrce.chatroom.shared.packet.impl.common;

import io.github.spigotrce.chatroom.shared.ProtocolUtil;
import io.github.spigotrce.chatroom.shared.packet.ImplDirection;
import io.github.spigotrce.chatroom.shared.packet.Packet;

public class ChatMessagePacket extends Packet {
    private String message;

    public ChatMessagePacket(ImplDirection implDirection) {
        super(implDirection, "chat_message");
        this.message = "";
    }

    public ChatMessagePacket(String message, ImplDirection implDirection) {
        this(implDirection);
        this.message = message;
    }

    @Override
    public String encode() {
        return ProtocolUtil.write(message);
    }

    @Override
    public String[] decode(String input) {
        return ProtocolUtil.read(input);
    }
}