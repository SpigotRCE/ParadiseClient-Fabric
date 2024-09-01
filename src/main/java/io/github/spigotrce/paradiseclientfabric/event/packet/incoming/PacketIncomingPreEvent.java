package io.github.spigotrce.paradiseclientfabric.event.packet.incoming;

import io.github.spigotrce.eventbus.event.Cancellable;
import io.github.spigotrce.eventbus.event.Event;
import net.minecraft.network.packet.Packet;

public class PacketIncomingPreEvent extends Event implements Cancellable {
    private boolean isCancel = false;
    private Packet<?> packet;

    public PacketIncomingPreEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    @Override
    public boolean isCancel() {
        return isCancel;
    }

    @Override
    public void setCancel(boolean b) {
        isCancel = b;
    }
}
