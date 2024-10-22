package io.github.spigotrce.paradiseclientfabric.listener;

import io.github.spigotrce.eventbus.event.EventHandler;
import io.github.spigotrce.eventbus.event.listener.Listener;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.channel.ChannelRegisterEvent;

public class ChannelListener implements Listener {
    @EventHandler
    public void onChannelRegister(ChannelRegisterEvent event) {
        String channel = event.getChannel();
        if (ParadiseClient_Fabric.getNetworkMod().registeredChannelsByName.contains(channel))
            event.setMessage("§9Channel: §4" + channel);
    }
}
