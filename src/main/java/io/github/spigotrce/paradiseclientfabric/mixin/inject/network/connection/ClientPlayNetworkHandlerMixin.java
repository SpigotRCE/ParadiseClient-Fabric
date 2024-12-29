package io.github.spigotrce.paradiseclientfabric.mixin.inject.network.connection;

import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.event.chat.ChatPostEvent;
import io.github.spigotrce.paradiseclientfabric.event.chat.ChatPreEvent;
import io.github.spigotrce.paradiseclientfabric.mixin.accessor.ClientPlayNetworkHandlerAccessor;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.message.LastSeenMessagesCollector;
import net.minecraft.network.message.MessageBody;
import net.minecraft.network.message.MessageChain;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;

/**
 * Mixin class to modify the behavior of the ClientPlayNetworkHandler class.
 * <p>
 * This class handles the game join event and updates connection information.
 * </p>
 *
 * @author SpigotRCE
 * @since 2.17
 */
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements ClientPlayNetworkHandlerAccessor {

    @Shadow
    private LastSeenMessagesCollector lastSeenMessagesCollector;

    @Shadow
    private MessageChain.Packer messagePacker;

    /**
     * Injects code at the end of the onGameJoin method to update connection status and server IP.
     * <p>
     * This method sets the connection status to true and updates the server IP address
     * when the game join packet is received.
     * </p>
     *
     * @param packet The game join packet received from the server.
     * @param info   The callback information.
     */
    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
        ParadiseClient_Fabric.networkMod.isConnected = true;
        ParadiseClient_Fabric.networkMod.serverIP = ((ClientPlayNetworkHandler) (Object) this).getConnection().getAddress().toString().split("/")[0];
        if (ParadiseClient_Fabric.miscMod.isClientOutdated)
            Helper.printChatMessage("&4Client is outdated! Latest version: &2" + ParadiseClient_Fabric.miscMod.latestVersion);
    }

    /**
     * This method fires the {@link ChatPreEvent}.
     *
     * @param content The content entered by the player.
     * @param ci      The callback information.
     */
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessageH(String content, CallbackInfo ci) {
        ChatPreEvent event = new ChatPreEvent(content);
        try {
            ParadiseClient_Fabric.eventManager.fireEvent(event);
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to fire ChatPreEvent", e);
        }
        if (event.isCancel()) ci.cancel();
    }

    /**
     * This method fires the {@link ChatPostEvent}.
     *
     * @param content The content entered by the player.
     * @param ci      The callback information.
     */
    @Inject(method = "sendChatMessage", at = @At("TAIL"))
    private void onSendChatMessageT(String content, CallbackInfo ci) {
        ChatPostEvent event = new ChatPostEvent(content);
        try {
            ParadiseClient_Fabric.eventManager.fireEvent(event);
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to fire ChatPreEvent", e);
        }
    }

    /**
     * Accessor method to send chat message internally without firing the chat events.
     * @param message The message to be sent.
     */
    @Override
    public void paradiseClient_Fabric$sendChatMessage(String message) {
        Instant instant = Instant.now();
        long l = NetworkEncryptionUtils.SecureRandomUtil.nextLong();
        LastSeenMessagesCollector.LastSeenMessages lastSeenMessages = this.lastSeenMessagesCollector.collect();
        MessageSignatureData messageSignatureData = this.messagePacker.pack(new MessageBody(message, instant, l, lastSeenMessages.lastSeen()));
        Helper.sendPacket(new ChatMessageC2SPacket(message, instant, l, messageSignatureData, lastSeenMessages.update()));
    }
}
