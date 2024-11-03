package io.github.spigotrce.paradiseclientfabric.mixin.accessor;

import io.github.spigotrce.paradiseclientfabric.event.chat.ChatPostEvent;
import io.github.spigotrce.paradiseclientfabric.event.chat.ChatPreEvent;

public interface ClientPlayNetworkHandlerAccessor {
    /**
     * Sends a chat message without firing the {@link ChatPostEvent} or {@link ChatPreEvent}.
     *
     * @param message The message to be sent.
     */
    void paradiseClient_Fabric$sendChatMessage(String message);
}
