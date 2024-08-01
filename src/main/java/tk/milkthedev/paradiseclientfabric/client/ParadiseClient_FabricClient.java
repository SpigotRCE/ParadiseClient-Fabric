package tk.milkthedev.paradiseclientfabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import tk.milkthedev.paradiseclientfabric.packet.ChatSentryPayloadPacket;

public class ParadiseClient_FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.playC2S().register(ChatSentryPayloadPacket.ID, ChatSentryPayloadPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(ChatSentryPayloadPacket.ID, ChatSentryPayloadPacket.CODEC);
    }
}
