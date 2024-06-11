package tk.milkthedev.paradiseclientfabric.mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import tk.milkthedev.paradiseclientfabric.mixin.accessor.SessionAccessor;

import static tk.milkthedev.paradiseclientfabric.Constants.LOGGER;

public class BungeeSpoofMod
{
    private final SessionAccessor sessionAccessor;
    private String bungeeUsername;
    private String bungeeFakeUsername;
    private boolean bungeeUUIDPremium;
    private String bungeeUUID;
    private String bungeeIP;
    private String targetIP; // yet to implement
    private String bungeeToken; // yet to implement
    private boolean bungeeEnabled;

    public BungeeSpoofMod()
    {
        LOGGER.info("BungeeSpoofMod initializing");
        MinecraftClient minecraft = MinecraftClient.getInstance();
        Session minecraftSession = minecraft.getSession();
        this.sessionAccessor = (SessionAccessor) minecraftSession;
        this.bungeeUsername = minecraftSession.getUsername();
        this.bungeeFakeUsername = this.bungeeUsername;
        this.bungeeUUIDPremium = false;
        this.bungeeUUID = minecraftSession.getUuidOrNull().toString().replace("-", "");
        this.bungeeIP = "1.3.3.7";
        this.targetIP = "0.0.0.0";
        this.bungeeToken = "";
        this.bungeeEnabled = false;
        LOGGER.info("BungeeSpoofMod initialized");
    }

    public String getBungeeUsername()
    {
        return bungeeUsername;
    }

    public void setBungeeUsername(String bungeeUsername)
    {
        this.bungeeUsername = bungeeUsername;
        this.sessionAccessor.paradiseClient_Fabric$setUsername(this.bungeeUsername);
    }

    public String getBungeeFakeUsername()
    {
        return bungeeFakeUsername;
    }

    public void setBungeeFakeUsername(String bungeeFakeUsername)
    {
        this.bungeeFakeUsername = bungeeFakeUsername;
    }

    public boolean isBungeeUUIDPremium()
    {
        return bungeeUUIDPremium;
    }

    public void setBungeeUUIDPremium(boolean bungeeUUIDPremium)
    {
        this.bungeeUUIDPremium = bungeeUUIDPremium;
    }

    public String getBungeeUUID()
    {
        return bungeeUUID;
    }

    public void setBungeeUUID(String bungeeUUID)
    {
        this.bungeeUUID = bungeeUUID;
    }

    public String getBungeeIP()
    {
        return bungeeIP;
    }

    public void setBungeeIP(String bungeeIP)
    {
        this.bungeeIP = bungeeIP;
    }

    public String getTargetIP()
    {
        return targetIP;
    }

    public void setTargetIP(String targetIP)
    {
        this.targetIP = targetIP;
    }

    public String getBungeeToken()
    {
        return bungeeToken;
    }

    public void setBungeeToken(String bungeeToken)
    {
        this.bungeeToken = bungeeToken;
    }

    public boolean isBungeeEnabled()
    {
        return bungeeEnabled;
    }

    public void setBungeeEnabled(boolean bungeeEnabled)
    {
        this.bungeeEnabled = bungeeEnabled;
    }
}
