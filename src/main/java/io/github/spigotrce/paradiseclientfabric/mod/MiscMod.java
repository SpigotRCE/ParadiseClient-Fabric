package io.github.spigotrce.paradiseclientfabric.mod;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.Packet;

/**
 * Manages miscellaneous data related to network packets and screen state.
 * <p>
 * This class keeps track of various network packet details and the current screen being displayed.
 * </p>
 *
 * @author SpigotRCE
 * @since 1.1
 */
public class MiscMod {
    /**
     * The last incoming network packet received.
     */
    public Packet<?> lastIncomingPacket;

    /**
     * The timestamp when the last incoming packet was received.
     */
    public long lastIncomingPacketTime;

    /**
     * The average delay of incoming network packets.
     */
    public long averageIncomingPacketDelay;

    /**
     * The last outgoing network packet sent.
     */
    public Packet<?> lastOutgoingPacket;

    /**
     * The timestamp when the last outgoing packet was sent.
     */
    public long lastOutgoingPacketTime;

    /**
     * The average delay of outgoing network packets.
     */
    public long averageOutgoingPacketDelay;

    /**
     * The last message sent or received.
     */
    public String lastMessage;

    /**
     * The current screen being displayed in the client.
     */
    public Screen currentScreen;
}
