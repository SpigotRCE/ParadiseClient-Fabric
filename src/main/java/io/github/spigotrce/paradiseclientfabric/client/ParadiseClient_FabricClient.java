package io.github.spigotrce.paradiseclientfabric.client;

import io.github.spigotrce.paradiseclientfabric.packet.VelocityReportPayloadPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

/**
 * This class is the entry point for the client-side functionality of the ParadiseClient mod.
 * It implements the {@link ClientModInitializer} interface, which is a FabricMC API for initializing client-side mods.
 *
 * <p>The {@link #onInitializeClient()} method is called when the client-side mod is initialized.
 * This method is overridden from the {@link ClientModInitializer} interface and should contain any necessary client-side initialization logic.
 *
 * @author SpigotRCE
 * @since 1.0
 */
public class ParadiseClient_FabricClient implements ClientModInitializer {
    /**
     * Initializes the client-side functionality of the ParadiseClient mod.
     * This method is called when the client-side mod is loaded.
     *
     * <p>No parameters are required for this method.
     *
     * <p>The return value is void, as there is no meaningful result to return from this method.
     */
    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.playC2S().register(VelocityReportPayloadPacket.ID, VelocityReportPayloadPacket.CODEC);
    }
}
