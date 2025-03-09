package io.github.spigotrce.paradiseclientfabric.chatroom.common.packet;

import io.github.spigotrce.paradiseclientfabric.chatroom.common.packet.handler.AbstractPacketHandler;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.UUID;

public abstract class Packet {
    public abstract void encode(ByteBuf buffer);

    public abstract void decode(ByteBuf buffer);

    public abstract void handle(AbstractPacketHandler handler) throws Exception;

    public void writeString(ByteBuf buffer, String value) {
        byte[] bytes = value.getBytes(Charset.defaultCharset());
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public String readString(ByteBuf buffer) {
        int length = buffer.readInt();
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return new String(bytes, Charset.defaultCharset());
    }

    public void writeBoolean(ByteBuf buffer, boolean value) {
        buffer.writeByte(value ? 1 : 0);
    }

    public boolean readBoolean(ByteBuf buffer) {
        return buffer.readByte() == 1;
    }

    public void writeLong(ByteBuf buffer, long value) {
        buffer.writeLong(value);
    }

    public long readLong(ByteBuf buffer) {
        return buffer.readLong();
    }

    public void writeInt(ByteBuf buffer, int value) {
        buffer.writeInt(value);
    }

    public int readInt(ByteBuf buffer) {
        return buffer.readInt();
    }

    public void writeShort(ByteBuf buffer, short value) {
        buffer.writeShort(value);
    }

    public short readShort(ByteBuf buffer) {
        return buffer.readShort();
    }

    public void writeByte(ByteBuf buffer, byte value) {
        buffer.writeByte(value);
    }

    public byte readByte(ByteBuf buffer) {
        return buffer.readByte();
    }

    public void writeDouble(ByteBuf buffer, double value) {
        buffer.writeDouble(value);
    }

    public double readDouble(ByteBuf buffer) {
        return buffer.readDouble();
    }

    public void writeFloat(ByteBuf buffer, float value) {
        buffer.writeFloat(value);
    }

    public float readFloat(ByteBuf buffer) {
        return buffer.readFloat();
    }

    public void writeUUID(ByteBuf buffer, UUID uuid) {
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
    }

    public UUID readUUID(ByteBuf buffer) {
        return new UUID(buffer.readLong(), buffer.readLong());
    }
}
