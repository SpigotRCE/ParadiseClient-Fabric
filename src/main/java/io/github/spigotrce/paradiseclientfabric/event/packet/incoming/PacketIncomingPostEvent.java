package io.github.spigotrce.paradiseclientfabric.event.packet.incoming;

import io.github.spigotrce.eventbus.event.Event;
import net.minecraft.network.packet.Packet;

public class PacketIncomingPostEvent extends Event {
    private final Packet<?> packet;

    public PacketIncomingPostEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
