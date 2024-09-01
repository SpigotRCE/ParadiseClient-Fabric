package io.github.spigotrce.paradiseclientfabric.mixin.inject.network;

import net.minecraft.network.packet.c2s.handshake.ConnectionIntent;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import io.github.spigotrce.paradiseclientfabric.mod.BungeeSpoofMod;

/**
 * Mixin class to modify the behavior of the HandshakeC2SPacket class.
 * <p>
 * This class handles packet initialization and modifies the address and additional
 * information based on BungeeCord spoofing settings.
 * </p>
 *
 * @author SpigotRCE
 * @since 1.0
 */
@Mixin(HandshakeC2SPacket.class)
public class HandshakeC2SMixin {

    @Mutable
    @Shadow
    @Final
    private String address;

    /**
     * Injects code into the constructor of the HandshakeC2SPacket class to modify
     * the address and append BungeeCord information if spoofing is enabled.
     * <p>
     * This method sets the address to the BungeeCord target IP and appends
     * BungeeCord information if BungeeCord spoofing is enabled and the connection
     * intent is LOGIN.
     * </p>
     *
     * @param i               The first parameter of the constructor.
     * @param string          The address string.
     * @param j               The second parameter of the constructor.
     * @param connectionIntent The connection intent of the handshake.
     * @param ci              The callback information.
     */
    @Inject(method = "<init>(ILjava/lang/String;ILnet/minecraft/network/packet/c2s/handshake/ConnectionIntent;)V", at = @At("RETURN"))
    private void HandshakeC2SPacket(int i, String string, int j, ConnectionIntent connectionIntent, CallbackInfo ci) {
        BungeeSpoofMod bungeeSpoofMod = ParadiseClient_Fabric.getBungeeSpoofMod();

        if (bungeeSpoofMod.isBungeeTargetEnabled()) {
            this.address = bungeeSpoofMod.getBungeeTargetIP();
        }
        if (bungeeSpoofMod.isBungeeEnabled() && connectionIntent == ConnectionIntent.LOGIN) {
            this.address += "\000" + bungeeSpoofMod.getBungeeIP() + "\000" + bungeeSpoofMod.getBungeeUUID();
        }
    }
}
