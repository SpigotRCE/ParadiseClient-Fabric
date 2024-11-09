package io.github.spigotrce.chatroom.shared.network.packet;


public abstract class Packet {
    private final String id;
    public byte[] buf;
    private AbstractPacketHandler handler;

    public Packet(String id) {
        this.id = id;
        buf = new byte[32_768];
    }

    public String getId() {
        return id;
    }

    public AbstractPacketHandler getAbstractPacketHandler() {
        return handler;
    }

    public void setAbstractPacketHandler(AbstractPacketHandler handler) {
        this.handler = handler;
    }

    public abstract void encode();

    public abstract void decode();

    public void apply() {
        handler.handle(this);
    }
}
