package tk.milkthedev.paradiseclientfabric;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

public class ParadiseClient_Fabric implements ModInitializer
{
    private static BungeeSpoofMod bungeeSpoofMod;
    private static MiscMod miscMod;

    public static BungeeSpoofMod getBungeeSpoofMod() {return bungeeSpoofMod;}
    public static MiscMod getMiscMod() {return miscMod;}

    @Override
    public void onInitialize()
    {
        bungeeSpoofMod = new BungeeSpoofMod();
        miscMod = new MiscMod();
    }
}
