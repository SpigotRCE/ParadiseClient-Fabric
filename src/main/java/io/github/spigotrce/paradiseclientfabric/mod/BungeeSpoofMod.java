package io.github.spigotrce.paradiseclientfabric.mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import io.github.spigotrce.paradiseclientfabric.mixin.accessor.SessionAccessor;

import static io.github.spigotrce.paradiseclientfabric.Constants.LOGGER;

/**
 * Handles BungeeCord spoofing operations.
 * <p>
 * This class manages BungeeCord spoofing settings, including the username, UUID, IP addresses, and token.
 * It initializes with values from the Minecraft session and provides methods to modify these values.
 * </p>
 *
 * @author SpigotRCE
 * @since 1.0
 */
public class BungeeSpoofMod {
    /** The session accessor for interacting with Minecraft's session. */
    private final SessionAccessor sessionAccessor;

    /** The username used for BungeeCord spoofing. */
    private String bungeeUsername;

    /** A fake username used for BungeeCord spoofing. */
    private String bungeeFakeUsername;

    /** Whether the BungeeCord UUID is for a premium account. */
    private boolean bungeeUUIDPremium;

    /** The UUID used for BungeeCord spoofing. */
    private String bungeeUUID;

    /** The IP address used for BungeeCord spoofing. */
    private String bungeeIP;

    /** The target IP address for BungeeCord spoofing. */
    private String targetIP;

    /** The token used for BungeeCord spoofing (yet to be implemented). */
    private String bungeeToken;

    /** Whether BungeeCord spoofing is enabled. */
    private boolean bungeeEnabled;

    /** Whether the target IP for BungeeCord spoofing is enabled. */
    private boolean bungeeTargetEnabled;

    /**
     * Constructs a new BungeeSpoofMod instance.
     * <p>
     * Initializes the mod with values from the current Minecraft session.
     * </p>
     */
    public BungeeSpoofMod() {
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

    /**
     * Gets the BungeeCord username.
     *
     * @return The BungeeCord username.
     */
    public String getBungeeUsername() {
        return bungeeUsername;
    }

    /**
     * Sets the BungeeCord username and updates the session.
     *
     * @param bungeeUsername The new BungeeCord username.
     */
    public void setBungeeUsername(String bungeeUsername) {
        this.bungeeUsername = bungeeUsername;
        this.sessionAccessor.paradiseClient_Fabric$setUsername(this.bungeeUsername);
    }

    /**
     * Gets the fake BungeeCord username.
     *
     * @return The fake BungeeCord username.
     */
    public String getBungeeFakeUsername() {
        return bungeeFakeUsername;
    }

    /**
     * Sets the fake BungeeCord username.
     *
     * @param bungeeFakeUsername The new fake BungeeCord username.
     */
    public void setBungeeFakeUsername(String bungeeFakeUsername) {
        this.bungeeFakeUsername = bungeeFakeUsername;
    }

    /**
     * Checks if the BungeeCord UUID is for a premium account.
     *
     * @return True if the UUID is premium, false otherwise.
     */
    public boolean isBungeeUUIDPremium() {
        return bungeeUUIDPremium;
    }

    /**
     * Sets whether the BungeeCord UUID is for a premium account.
     *
     * @param bungeeUUIDPremium True if the UUID is premium, false otherwise.
     */
    public void setBungeeUUIDPremium(boolean bungeeUUIDPremium) {
        this.bungeeUUIDPremium = bungeeUUIDPremium;
    }

    /**
     * Gets the BungeeCord UUID.
     *
     * @return The BungeeCord UUID.
     */
    public String getBungeeUUID() {
        return bungeeUUID;
    }

    /**
     * Sets the BungeeCord UUID.
     *
     * @param bungeeUUID The new BungeeCord UUID.
     */
    public void setBungeeUUID(String bungeeUUID) {
        this.bungeeUUID = bungeeUUID;
    }

    /**
     * Gets the BungeeCord IP address.
     *
     * @return The BungeeCord IP address.
     */
    public String getBungeeIP() {
        return bungeeIP;
    }

    /**
     * Sets the BungeeCord IP address.
     *
     * @param bungeeIP The new BungeeCord IP address.
     */
    public void setBungeeIP(String bungeeIP) {
        this.bungeeIP = bungeeIP;
    }

    /**
     * Gets the target IP address for BungeeCord spoofing.
     *
     * @return The target IP address.
     */
    public String getBungeeTargetIP() {
        return targetIP;
    }

    /**
     * Sets the target IP address for BungeeCord spoofing.
     *
     * @param targetIP The new target IP address.
     */
    public void setTargetIP(String targetIP) {
        this.targetIP = targetIP;
    }

    /**
     * Gets the BungeeCord token.
     *
     * @return The BungeeCord token.
     */
    public String getBungeeToken() {
        return bungeeToken;
    }

    /**
     * Sets the BungeeCord token.
     *
     * @param bungeeToken The new BungeeCord token.
     */
    public void setBungeeToken(String bungeeToken) {
        this.bungeeToken = bungeeToken;
    }

    /**
     * Checks if BungeeCord spoofing is enabled.
     *
     * @return True if BungeeCord spoofing is enabled, false otherwise.
     */
    public boolean isBungeeEnabled() {
        return bungeeEnabled;
    }

    /**
     * Sets whether BungeeCord spoofing is enabled.
     *
     * @param bungeeEnabled True if BungeeCord spoofing is enabled, false otherwise.
     */
    public void setBungeeEnabled(boolean bungeeEnabled) {
        this.bungeeEnabled = bungeeEnabled;
    }

    /**
     * Checks if the target IP for BungeeCord spoofing is enabled.
     *
     * @return True if the target IP is enabled, false otherwise.
     */
    public boolean isBungeeTargetEnabled() {
        return bungeeTargetEnabled;
    }

    /**
     * Sets whether the target IP for BungeeCord spoofing is enabled.
     *
     * @param bungeeTargetEnabled True if the target IP is enabled, false otherwise.
     */
    public void setBungeeTargetEnabled(boolean bungeeTargetEnabled) {
        this.bungeeTargetEnabled = bungeeTargetEnabled;
    }
}
