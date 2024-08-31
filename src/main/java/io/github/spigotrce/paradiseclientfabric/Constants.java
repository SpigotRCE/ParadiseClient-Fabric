package io.github.spigotrce.paradiseclientfabric;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    public static final String VERSION = "2.21.1";
    public static final String MOD_ID = "paradiseclient-fabric";
    public static final String MOD_NAME = "ParadiseClient-Fabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final Identifier backgroundImage = Identifier.of(Constants.MOD_ID, "wallpaper.png");

    public static String EDITION = "PUBLIC"; // For API
}
