package io.github.spigotrce.paradiseclientfabric.listener;

import io.github.spigotrce.eventbus.event.EventHandler;
import io.github.spigotrce.eventbus.event.listener.Listener;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.packet.incoming.PacketIncomingPreEvent;
import io.github.spigotrce.paradiseclientfabric.event.packet.outgoing.PacketOutgoingPostEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;

import java.util.List;

@SuppressWarnings("unused")
public class PacketListener implements Listener {
    @EventHandler
    public void onResourcePackPacketReceive(PacketIncomingPreEvent event) {
        if (!(event.getPacket() instanceof ResourcePackSendS2CPacket packet)) return;
        String url = packet.url();
        Helper.printChatMessage("Server resource pack url: " + url);
    }

    @EventHandler
    public void onIncomingPacketReceive(PacketIncomingPreEvent event) {
        if (!(event.getPacket() instanceof CommandSuggestionsS2CPacket packet)) return;
        if (packet.id() != 1234689045) return;
        Helper.printChatMessage("Command suggestions received! Dumping");
        List<CommandSuggestionsS2CPacket.Suggestion> suggestions = packet.suggestions();

        new Thread(() -> {
            try {
                suggestions.forEach(suggestion -> {
                    MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("ip " + suggestion.text());
                });
            } catch (Exception ignored) {
            }
        }).start();
    }

    @EventHandler
    public void onOutgoingPacketReceive(PacketOutgoingPostEvent event) {
        ParadiseClient_Fabric.NETWORK_MOD.lastOutgoingPacket = event.getPacket();
        ParadiseClient_Fabric.NETWORK_MOD.lastOutgoingPacketTime = System.currentTimeMillis();
    }
}
