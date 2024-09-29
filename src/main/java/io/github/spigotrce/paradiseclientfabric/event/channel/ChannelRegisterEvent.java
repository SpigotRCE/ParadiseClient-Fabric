package io.github.spigotrce.paradiseclientfabric.event.channel;

import io.github.spigotrce.eventbus.event.Cancellable;
import io.github.spigotrce.eventbus.event.Event;

public class ChannelRegisterEvent extends Event implements Cancellable {
    private boolean isCancel = false;
    private String channel;
    private String message;

    public ChannelRegisterEvent(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
