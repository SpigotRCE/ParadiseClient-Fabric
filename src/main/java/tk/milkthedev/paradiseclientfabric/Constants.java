package tk.milkthedev.paradiseclientfabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants
{
    public static final String VERSION = "1.9";
    public static final String CHANGES = "Fixed bug in chatroom when current player is null. Added wallpaper in multiplayer screen. Fixed so ip forward wont happe n for server ping request. Made offline UUID not case sensitive"; // same as the commit name/description
    public static final String MOD_ID = "paradiseclient-fabric";
    public static final String MOD_NAME = "ParadiseClient-Fabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
}
