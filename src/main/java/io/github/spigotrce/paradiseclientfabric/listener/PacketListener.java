package io.github.spigotrce.paradiseclientfabric.listener;

import io.github.spigotrce.eventbus.event.EventHandler;
import io.github.spigotrce.eventbus.event.listener.Listener;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.packet.incoming.PacketIncomingPostEvent;
import io.github.spigotrce.paradiseclientfabric.event.packet.incoming.PacketIncomingPreEvent;
import io.github.spigotrce.paradiseclientfabric.event.packet.outgoing.PacketOutgoingPostEvent;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;

@SuppressWarnings("unused")
public class PacketListener implements Listener {
    @EventHandler
    public void onResourcePackPacketReceive(PacketIncomingPreEvent event) {
        if (!(event.getPacket() instanceof ResourcePackSendS2CPacket packet)) return;
        String url = packet.url();
        Helper.printChatMessage("Server resource pack url: " + url);
    }

    @EventHandler
    public void onOutgoingPacketReceive(PacketOutgoingPostEvent event) {
        ParadiseClient_Fabric.getNetworkMod().lastOutgoingPacket = event.getPacket();
        ParadiseClient_Fabric.getNetworkMod().lastOutgoingPacketTime = System.currentTimeMillis();
    }
}
