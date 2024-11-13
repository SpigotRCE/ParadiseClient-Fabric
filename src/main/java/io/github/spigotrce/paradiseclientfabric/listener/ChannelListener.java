package io.github.spigotrce.paradiseclientfabric.listener;

import io.github.spigotrce.eventbus.event.EventHandler;
import io.github.spigotrce.eventbus.event.listener.Listener;
import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.channel.PluginMessageEvent;
import net.minecraft.network.PacketByteBuf;

import java.nio.charset.Charset;
import java.util.Objects;

public class ChannelListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onChannelRegister(PluginMessageEvent event) {
        String channelName = event.getChannel();
        byte[] buf = event.getBuf();
        String data = new String(buf);
        try {
            if (Objects.equals(channelName, "minecraft:register") || Objects.equals(channelName, "REGISTER")) // 1.13 channel or 1.8 channel
                for (String s : data.split("\000"))
                    Helper.printChatMessage("&fChannel: &" + (ParadiseClient_Fabric.getNetworkMod().getRegisteredChannelsByName().contains(s) ? "c " : "d ") + s);
            else
                Helper.printChatMessage("&fChannel: &d" + channelName + " &fData: &d" + data);
        } catch (Exception e) {
            Helper.printChatMessage("&4Error handling listener for payload for channel: " + channelName + " " + e.getMessage());
            Constants.LOGGER.error("&4Error handling listener for channel: {} {}", channelName, e);

        }
    }
}
