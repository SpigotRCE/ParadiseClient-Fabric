package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.impl;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.model.UserModel;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.Packet;
import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.Date;
import java.util.UUID;

public class HandshakeResponsePacket extends Packet {
    private UserModel userModel;
    private boolean success;

    public HandshakeResponsePacket(UserModel userModel, boolean success) {
        this.userModel = userModel;
        this.success = success;
    }

    public HandshakeResponsePacket() {
        this.userModel = null;
        this.success = false;
    }

    @Override
    public void encode(ByteBuf buffer) {
        writeBoolean(buffer, success);
        writeLong(buffer, userModel.discordID());
        writeUUID(buffer, userModel.uuid());
        writeLong(buffer, userModel.dateOfRegistration().getTime());
        writeString(buffer, userModel.username());
        writeString(buffer, userModel.email());
        writeString(buffer, userModel.token());
        writeBoolean(buffer, userModel.verified());
    }

    @Override
    public void decode(ByteBuf buffer) {
        success = readBoolean(buffer);
        long discordID = readLong(buffer);
        UUID uuid = readUUID(buffer);
        Date dateOfRegistration = new Date(readLong(buffer));
        String username = readString(buffer);
        String email = readString(buffer);
        String token = readString(buffer);
        boolean verified = readBoolean(buffer);

        userModel = new UserModel(discordID, uuid, dateOfRegistration, username, email, token, verified);
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
