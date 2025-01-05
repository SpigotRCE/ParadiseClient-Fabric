package io.github.spigotrce.paradiseclientfabric.mod;

import io.github.spigotrce.paradiseclientfabric.mixin.accessor.SessionAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;

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
    /**
     * The session accessor for interacting with Minecraft's session.
     */
    public final SessionAccessor sessionAccessor;

    /**
     * The username used for BungeeCord spoofing.
     */
    public String usernameReal;

    /**
     * A fake username used for BungeeCord spoofing.
     */
    public String usernameFake;

    /**
     * Whether the BungeeCord UUID is for a premium account.
     */
    public boolean isUUIDOnline;

    /**
     * The UUID used for BungeeCord spoofing.
     */
    public String uuid;

    /**
     * The IP address used for BungeeCord spoofing.
     */
    public String ip;

    /**
     * The target IP address for BungeeCord spoofing.
     */
    public String hostname;

    /**
     * The token used for BungeeCord spoofing (yet to be implemented).
     */
    public String token;

    /**
     * Whether BungeeCord spoofing is enabled.
     */
    public boolean isIPForwarding;

    /**
     * Whether the target IP for BungeeCord spoofing is enabled.
     */
    public boolean isHostnameForwarding;

    /**
     * Constructs a new BungeeSpoofMod instance.
     * <p>
     * Initializes the mod with values from the current Minecraft session.
     * </p>
     */
    public BungeeSpoofMod() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        Session minecraftSession = minecraft.getSession();
        this.sessionAccessor = (SessionAccessor) minecraftSession;
        this.usernameReal = minecraftSession.getUsername();
        this.usernameFake = this.usernameReal;
        this.isUUIDOnline = false;
        this.uuid = minecraftSession.getUuidOrNull().toString().replace("-", "");
        this.ip = "1.3.3.7";
        this.hostname = "0.0.0.0";
        this.token = "";
        this.isIPForwarding = false;
        this.isHostnameForwarding = false;
    }
}
