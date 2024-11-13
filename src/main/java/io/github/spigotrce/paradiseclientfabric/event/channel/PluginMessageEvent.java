package io.github.spigotrce.paradiseclientfabric.event.channel;

import io.github.spigotrce.eventbus.event.Cancellable;
import io.github.spigotrce.eventbus.event.Event;
import net.minecraft.network.PacketByteBuf;

public class PluginMessageEvent extends Event implements Cancellable {
    private boolean isCancel = false;
    private final String channel;
    private final byte[] buf;

    public PluginMessageEvent(String channel, byte[] buf) {
        this.channel = channel;
        this.buf = buf;
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getBuf() {
        return buf;
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
