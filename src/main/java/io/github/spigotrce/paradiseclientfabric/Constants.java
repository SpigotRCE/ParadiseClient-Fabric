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

import java.util.ArrayList;

public class Constants {
    /**
     * The version of the mod.
     */
    public static final String VERSION = "1.21.4-3-9";

    /**
     * The ID of the mod.
     */
    public static final String MOD_ID = "paradiseclient-fabric";

    /**
     * The name of the mod.
     */
    public static final String MOD_NAME = "ParadiseClient-Fabric";

    /**
     * Window title
     * Not final because MiscMod#isClientOutdated is dynamic
     */
    public static String WINDOW_TITLE = MOD_NAME + " [" + Constants.EDITION + "] " + Constants.VERSION;

    /**
     * The logger for the mod.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ArrayList<Identifier> backgroundImages = new ArrayList<>();
    /**
     * The edition of the mod.
     */
    public static String EDITION = "PUBLIC"; // For API
}
