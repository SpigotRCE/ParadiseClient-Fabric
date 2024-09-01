package io.github.spigotrce.paradiseclientfabric.mod;

/**
 * Manages network connection state and server information.
 * <p>
 * This class tracks whether the client is connected to a server and stores the server's IP address.
 * </p>
 *
 * @author SpigotRCE
 * @since 2.17
 */
public class NetworkMod {
    /**
     * Indicates whether the client is currently connected to a server.
     */
    public boolean isConnected = false;

    /**
     * The IP address of the server the client is connected to.
     */
    public String serverIP = "";
}
