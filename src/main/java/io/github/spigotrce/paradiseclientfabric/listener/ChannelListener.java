package io.github.spigotrce.paradiseclientfabric.listener;

import io.github.spigotrce.eventbus.event.EventHandler;
import io.github.spigotrce.eventbus.event.listener.Listener;
import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.channel.PluginMessageEvent;
import net.minecraft.network.PacketByteBuf;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChannelListener implements Listener {
    private static final Set<String> detectedChannels = new HashSet<>(); // Stores detected channels

    @SuppressWarnings("unused")
    @EventHandler
    public void onChannelRegister(PluginMessageEvent event) {
        String channelName = event.getChannel();
        PacketByteBuf buf = event.getBuf();

        try {
            if (Objects.equals(channelName, "minecraft:register") || Objects.equals(channelName, "REGISTER")) {
                for (String splitted : buf.toString(Charset.defaultCharset()).split("\000")) {
                    detectedChannels.add(splitted); // Adds the channel to the list
                    Helper.printChatMessage("&fDetected channel: &d" + splitted);
                }
            } else {
                detectedChannels.add(channelName);
                Helper.printChatMessage("&fDetected channel: &d" + channelName);
            }
        } catch (Exception e) {
            Helper.printChatMessage("&4Error handling listener for channel: " + channelName + " " + e.getMessage());
            Constants.LOGGER.error("&4Error on channel: {} {}", channelName, e);
        }
    }

    public static Set<String> getDetectedChannels() {
        return detectedChannels;
    }

    public static void addChannel(String channelName) {
        detectedChannels.add(channelName);
        Helper.printChatMessage("&aManually added channel: &d" + channelName);
    }
}
