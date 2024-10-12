package io.github.spigotrce.chatroom.shared.packet;


public abstract class Packet {

    protected final PacketType type; // Packet type
    protected PacketManager manager; // Packet manager instance
    public final String delimiter; // String delimiter

    /**
     * <p>Creates a new Packet instance.</p>
     * <p>Field type should be set by the class implementation.</p>
     *
     * @param type type of the packet, example DISCONNECT, HANDSHAKE, UPDATE_PLAYER_COUNT etc.
     * @param manager the manager to manage packets
     */
    public Packet(PacketType type, PacketManager manager) {
        this.type = type;
        this.manager = manager;
        this.delimiter = "\000";
    }

    /**
     * <p>Reads the data from the raw packet</p>
     *
     * @param direction client or server direction
     * @param raw raw packet
     */
    public abstract void reader(PacketDirection direction, String raw);

    /**
     * <p>Writes the data in the raw format.</p>
     *
     * @param direction client or server direction
     * @return final packet to be sent to the server
     */
    public abstract String writer(PacketDirection direction);

    public String getPacketDisplayName() {
        return this.type.name();
    }

    /**
     * <p>Gets the packet's id.</p>
     *
     * @return the packet's id
     */
    public int getPacketId() {
        return this.type.ordinal();
    }

    /**
     * <p>Gets the packet type.</p>
     *
     * @return the packet type
     */
    public PacketType getType() {
        return this.type;
    }
}
