package tk.milkthedev.paradiseclientfabric;

import net.minecraft.network.packet.Packet;

public class MiscMod
{
    public Packet<?> lastIncomingPacket;
    public long lastIncomingPacketTime;
    public long averageIncomingPacketDelay;
    public Packet<?> lastOutgoingPacket;
    public long lastOutgoingPacketTime;
    public long averageOutgoingPacketDelay;
}
