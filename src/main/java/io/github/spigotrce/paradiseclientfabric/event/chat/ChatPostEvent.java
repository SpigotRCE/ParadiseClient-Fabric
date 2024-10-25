package io.github.spigotrce.paradiseclientfabric.event.chat;

import io.github.spigotrce.eventbus.event.Event;

@SuppressWarnings("unused")
public class ChatPostEvent extends Event {
    private final String message;

    public ChatPostEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
