package io.github.spigotrce.paradiseclientfabric.mod;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.netty.channel.Channel;

public class ChatRoomMod {
    public boolean isConnected = false;
    public Channel channel;
    public String token;
    public UserModel user;
}
