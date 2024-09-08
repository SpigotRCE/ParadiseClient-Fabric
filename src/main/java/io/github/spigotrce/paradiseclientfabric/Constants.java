/**
 * This class contains various constants used throughout the ParadiseClient-Fabric mod.
 *
 * @author SpigotRCE
 * @version 2.22
 * @since 1.0
 */
package io.github.spigotrce.paradiseclientfabric;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    /**
     * The version of the mod.
     */
    public static final String VERSION = "2.28.2";

    /**
     * The ID of the mod.
     */
    public static final String MOD_ID = "paradiseclient-fabric";

    /**
     * The name of the mod.
     */
    public static final String MOD_NAME = "ParadiseClient-Fabric";

    /**
     * The logger for the mod.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    /**
     * The identifier for the background image.
     */
    public static final Identifier backgroundImage = Identifier.of(Constants.MOD_ID, "wallpaper.png");

    /**
     * The edition of the mod.
     */
    public static String EDITION = "PUBLIC"; // For API
}