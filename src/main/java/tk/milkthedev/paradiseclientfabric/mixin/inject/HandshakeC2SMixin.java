package tk.milkthedev.paradiseclientfabric.mixin.inject;

import net.minecraft.network.packet.c2s.handshake.ConnectionIntent;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tk.milkthedev.paradiseclientfabric.mod.BungeeSpoofMod;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;

import static tk.milkthedev.paradiseclientfabric.Constants.LOGGER;

@Mixin(HandshakeC2SPacket.class)
public class HandshakeC2SMixin
{
    @Mutable
    @Shadow
    @Final
    private String address;

    @Inject(method = "<init>(ILjava/lang/String;ILnet/minecraft/network/packet/c2s/handshake/ConnectionIntent;)V", at = @At("RETURN"))
    private void HandshakeC2SPacket(int i, String string, int j, ConnectionIntent connectionIntent, CallbackInfo ci)
    {
        BungeeSpoofMod bungeeSpoofMod = ParadiseClient_Fabric.getBungeeSpoofMod();
        if (bungeeSpoofMod.isBungeeEnabled() && connectionIntent == ConnectionIntent.LOGIN)
        {
            LOGGER.info("Spoofing into HandshakeC2SPacket");
            this.address += "\000" + bungeeSpoofMod.getBungeeIP() + "\000" + bungeeSpoofMod.getBungeeUUID();
        }
    }
}