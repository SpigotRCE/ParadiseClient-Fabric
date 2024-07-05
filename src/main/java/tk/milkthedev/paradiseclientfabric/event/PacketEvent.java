package tk.milkthedev.paradiseclientfabric.event;

import net.minecraft.network.packet.Packet;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.mod.MiscMod;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;

public class PacketEvent
{
    private static final MiscMod miscMod = ParadiseClient_Fabric.getMiscMod();

    public static boolean PreIncomingPacket(Packet<?> packet)
    {
        return true;// false means cancellation
    }

    public static void PostIncomingPacket(Packet<?> packet)
    {
        miscMod.averageIncomingPacketDelay = System.currentTimeMillis() - miscMod.lastIncomingPacketTime;
        miscMod.lastIncomingPacketTime = System.currentTimeMillis();
        miscMod.lastIncomingPacket = packet;
    }


    public static boolean PreOutgoingPacket(Packet<?> packet)
    {
        return true; // false means cancellation
    }

    public static void PostOutgoingPacket(Packet<?> packet)
    {
        miscMod.averageOutgoingPacketDelay = System.currentTimeMillis() - miscMod.lastOutgoingPacketTime;
        miscMod.lastOutgoingPacketTime = System.currentTimeMillis();
        miscMod.lastOutgoingPacket = packet;
    }
}
