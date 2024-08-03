package tk.milkthedev.paradiseclientfabric.mod;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.Packet;

public class MiscMod {
    public Packet<?> lastIncomingPacket;
    public long lastIncomingPacketTime;
    public long averageIncomingPacketDelay;
    public Packet<?> lastOutgoingPacket;
    public long lastOutgoingPacketTime;
    public long averageOutgoingPacketDelay;
    public String lastMessage;
    public Screen currentScreen;
}
