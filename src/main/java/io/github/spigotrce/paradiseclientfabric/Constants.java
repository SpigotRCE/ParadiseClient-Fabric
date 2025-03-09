/**
 * This class contains various constants used throughout the ParadiseClient-Fabric mod.
 *
 * @author SpigotRCE
 * @version 2.22
 * @since 1.0
 */
package io.github.spigotrce.paradiseclientfabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    /**
     * The version of the mod.
     */
    public static final String VERSION = "1.21.4-3-19";

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
     * The edition of the mod.
     */
    public static String EDITION = "PUBLIC"; // For API

    /**
     * Window title
     * Not final because MiscMod#isClientOutdated is dynamic
     */
    public static String WINDOW_TITLE = MOD_NAME + " [" + Constants.EDITION + "] " + Constants.VERSION;

    public static void reloadTitle() {
        WINDOW_TITLE = Constants.MOD_NAME + " [" + Constants.EDITION + "] " + Constants.VERSION + " " +
                (ParadiseClient_Fabric.MISC_MOD.isClientOutdated ? "Outdated" : "");
    }
}
