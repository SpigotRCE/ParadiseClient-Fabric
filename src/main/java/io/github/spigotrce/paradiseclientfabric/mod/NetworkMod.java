package io.github.spigotrce.paradiseclientfabric.mod;

import net.minecraft.network.packet.Packet;

import java.util.ArrayList;

/**
 * Manages network connection state and server information.
 * <p>
 * This class tracks whether the client is connected to a server and stores the server's IP address.
 * </p>
 *
 * @author SpigotRCE
 * @since 2.17
 */
public class NetworkMod {
    /**
     * Indicates whether the client is currently connected to a server.
     */
    public boolean isConnected = false;

    /**
     * The IP address of the server the client is connected to.
     */
    public String serverIP = "";

    /**
     * The last incoming network packet received.
     */
    public Packet<?> lastIncomingPacket;

    /**
     * The timestamp when the last incoming packet was received.
     */
    public long lastIncomingPacketTime = 0; // Disabled temporarily;

    /**
     * The last outgoing network packet sent.
     */
    public Packet<?> lastOutgoingPacket;

    /**
     * The timestamp when the last outgoing packet was sent.
     */
    public long lastOutgoingPacketTime = 0; // Disabled temporarily;

    /**
     * The registered channels by their names.
     */
    public ArrayList<String> registeredChannelsByName = new ArrayList<>();
}
