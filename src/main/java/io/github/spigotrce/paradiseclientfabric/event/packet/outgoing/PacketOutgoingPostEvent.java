package io.github.spigotrce.paradiseclientfabric.event.packet.outgoing;

import io.github.spigotrce.eventbus.event.Event;
import net.minecraft.network.packet.Packet;

public class PacketOutgoingPostEvent extends Event {
    private final Packet<?> packet;

    public PacketOutgoingPostEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
