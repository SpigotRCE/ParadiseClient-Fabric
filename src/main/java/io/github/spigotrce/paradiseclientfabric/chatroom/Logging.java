package io.github.spigotrce.paradiseclientfabric.chatroom;

import java.util.ArrayList;
import java.util.Arrays;

public class Logging {
    public static void debug(String message) {
        // TODO: Add config option to enable/disable debugging
        System.out.println("[DEBUG/ChatServer] " + message);
    }

    public static void info(String message) {
        System.out.println("[INFO/ChatServer] " + message);
    }

    public static void earning(String message) {
        System.out.println("[WARNING/ChatServer] " + message);
    }

    public static void warning(String message, Exception exception) {
        System.out.println("[WARNING/ChatServer] " + message + ": " + exception.getMessage());
        new ArrayList<>(
                Arrays.asList(exception.getStackTrace())
        ).forEach(
                traceLine -> System.out.println("[WARNING/ChatServer] " + traceLine)
        );
    }

    public static void error(String message, Exception exception) {
        System.out.println("[ERROR/ChatServer] " + message + ": " + exception.getMessage());
        new ArrayList<>(
                Arrays.asList(exception.getStackTrace())
        ).forEach(
                traceLine -> System.out.println("[ERROR/ChatServer] " + traceLine)
        );
    }
}
