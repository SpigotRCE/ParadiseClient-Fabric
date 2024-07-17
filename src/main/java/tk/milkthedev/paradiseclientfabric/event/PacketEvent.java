package tk.milkthedev.paradiseclientfabric.event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import tk.milkthedev.paradiseclientfabric.mod.ExploitMod;
import tk.milkthedev.paradiseclientfabric.mod.MiscMod;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;

public class PacketEvent {
    private static final MiscMod miscMod = ParadiseClient_Fabric.getMiscMod();
    private static final ExploitMod exploitMod = ParadiseClient_Fabric.getExploitMod();
    private static final String signPayload = "{\"translate\":\"%2$s%2$s%2$s%2$s%2$s\"," +
            "\"with\":[\"\",{\"translate\":\"%2$s%2$s%2$s%2$s%2$s\"," +
            "\"with\":[\"\",{\"translate\":\"%2$s%2$s%2$s%2$s%2$s\"," +
            "\"with\":[\"\",{\"translate\":\"%2$s%2$s%2$s%2$s%2$s\"," +
            "\"with\":[\"\",{\"translate\":\"%2$s%2$s%2$s%2$s%2$s\"," +
            "\"with\":[\"\",{\"translate\":\"%2$s%2$s%2$s%2$s\"," +
            "\"with\":[\"\",{\"translate\":\"%2$s%2$s%2$s%2$s\"," +
            "\"with\":[\"\",{\"translate\":\"%2$s%2$s%2$s%2$s\"," +
            "\"with\":[\"a\",\"a\"]}]}]}]}]}]}]}]}";

    public static boolean PreIncomingPacket(Packet<?> packet) {
        return true;// false means cancellation
    }

    public static void PostIncomingPacket(Packet<?> packet) {
        miscMod.averageIncomingPacketDelay = System.currentTimeMillis() - miscMod.lastIncomingPacketTime;
        miscMod.lastIncomingPacketTime = System.currentTimeMillis();
        miscMod.lastIncomingPacket = packet;
    }


    public static boolean PreOutgoingPacket(Packet<?> packet) {
        if (packet instanceof UpdateSignC2SPacket && exploitMod.isSignExploitRunning) {
            exploitMod.isSignExploitRunning = false;
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(
                    new UpdateSignC2SPacket(
                            ((UpdateSignC2SPacket) packet).getPos(),
                            ((UpdateSignC2SPacket) packet).isFront(),
                            signPayload,
                            signPayload,
                            signPayload,
                            signPayload
                    ));
            System.out.println(signPayload);
            return false;
        }
        return true;
    }

    public static void PostOutgoingPacket(Packet<?> packet) {
        miscMod.averageOutgoingPacketDelay = System.currentTimeMillis() - miscMod.lastOutgoingPacketTime;
        miscMod.lastOutgoingPacketTime = System.currentTimeMillis();
        miscMod.lastOutgoingPacket = packet;
    }
}
