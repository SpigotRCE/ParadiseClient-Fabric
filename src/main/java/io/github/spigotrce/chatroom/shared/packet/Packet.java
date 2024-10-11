package io.github.spigotrce.chatroom.shared.packet;

import java.util.ArrayList;

public abstract class Packet {
    private final ImplDirection implDirection;
    private final String name;

    public Packet(ImplDirection implDirection, String name) {
        this.implDirection = implDirection;
        this.name = name;
    }

    public ImplDirection getImplDirection() {
        return implDirection;
    }

    public String getName() {
        return name;
    }

    public abstract String encode();

    public abstract String[] decode(String input);

    public String getDelimiter() {
        return "\000";
    }
}
