package tk.milkthedev.paradiseclientfabric;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

public class ParadiseClient_Fabric implements ModInitializer
{
    public static BungeeSpoofMod bungeeSpoofMod;

    public static BungeeSpoofMod getBungeeSpoofMod() {return bungeeSpoofMod;}

    @Override
    public void onInitialize() {bungeeSpoofMod = new BungeeSpoofMod();}
}
