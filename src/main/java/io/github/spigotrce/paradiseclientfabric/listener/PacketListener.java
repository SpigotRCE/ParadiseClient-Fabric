package io.github.spigotrce.paradiseclientfabric.listener;

import io.github.spigotrce.eventbus.event.EventHandler;
import io.github.spigotrce.eventbus.event.listener.Listener;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.packet.incoming.PacketIncomingPostEvent;
import io.github.spigotrce.paradiseclientfabric.event.packet.outgoing.PacketOutgoingPostEvent;

@SuppressWarnings("unused")
public class PacketListener implements Listener {
    @EventHandler
    public void onIncomingPacketReceive(PacketIncomingPostEvent event) {
        ParadiseClient_Fabric.getNetworkMod().lastIncomingPacket = event.getPacket();
        ParadiseClient_Fabric.getNetworkMod().lastIncomingPacketTime = System.currentTimeMillis();
    }

    @EventHandler
    public void onOutgoingPacketReceive(PacketOutgoingPostEvent event) {
        ParadiseClient_Fabric.getNetworkMod().lastOutgoingPacket = event.getPacket();
        ParadiseClient_Fabric.getNetworkMod().lastOutgoingPacketTime = System.currentTimeMillis();
    }
}
