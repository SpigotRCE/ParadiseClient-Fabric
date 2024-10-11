package io.github.spigotrce.chatroom.shared.packet.impl.client;

import io.github.spigotrce.chatroom.shared.ProtocolUtil;
import io.github.spigotrce.chatroom.shared.packet.ImplDirection;
import io.github.spigotrce.chatroom.shared.packet.Packet;

public class LoginStart extends Packet {
    private String username = "";
    private String password = "";

    public LoginStart(ImplDirection implDirection) {
        super(implDirection, "login_start");
    }

    public LoginStart(String s1, String s2) {
        this(ImplDirection.CLIENT);
        this.username = s1;
        this.password = s2;
    }

    @Override
    public String encode() {
        return ProtocolUtil.write(username, password);
    }

    @Override
    public String[] decode(String input) {
        return ProtocolUtil.read(input);
    }
}
