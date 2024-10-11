package io.github.spigotrce.chatroom.shared.packet.impl.server;

import io.github.spigotrce.chatroom.shared.ProtocolUtil;
import io.github.spigotrce.chatroom.shared.packet.ImplDirection;
import io.github.spigotrce.chatroom.shared.packet.Packet;

public class LoginResponse extends Packet {
    private boolean success = false;
    private String failureMessage = "";

    public LoginResponse(ImplDirection implDirection) {
        super(implDirection, "login_response");
    }

    public LoginResponse(boolean b1, String s1) {
        this(ImplDirection.SERVER);
        this.success = b1;
        this.failureMessage = s1;
    }

    @Override
    public String encode() {
        return ProtocolUtil.write(String.valueOf(success), failureMessage);
    }

    @Override
    public String[] decode(String input) {
        return ProtocolUtil.read(input);
    }
}
