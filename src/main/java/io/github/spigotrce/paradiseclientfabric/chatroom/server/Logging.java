package io.github.spigotrce.paradiseclientfabric.chatroom.server;

import java.util.ArrayList;
import java.util.Arrays;

public class Logging {
    public static void debug(String message) {
        System.out.println("\u001B[32m[DEBUG/ChatServer] " + message + "\u001B[0m");
    }

    public static void info(String message) {
        System.out.println("[INFO/ChatServer] " + message);
    }

    public static void warning(String message) {
        System.out.println("\u001B[33m[WARNING/ChatServer] " + message + "\u001B[0m");
    }

    public static void warning(String message, Exception exception) {
        System.out.println("\u001B[33m[WARNING/ChatServer] " + message + ": " + exception.getMessage() + "\u001B[0m");
        new ArrayList<>(
                Arrays.asList(exception.getStackTrace())
        ).forEach(
                traceLine -> System.out.println("\u001B[33m[WARNING/ChatServer] Stack trace ->" + traceLine + "\u001B[0m")
        );
    }

    public static void error(String message, Exception exception) {
        System.out.println("[ERROR/ChatServer] " + message + ": " + exception.getMessage());
        new ArrayList<>(
                Arrays.asList(exception.getStackTrace())
        ).forEach(
                traceLine -> System.out.println("\u001B[31m[ERROR/ChatServer] Stack trace ->" + traceLine + "\u001B[0m")
        );
    }

    public static void error(String message, Throwable throwable) {
        System.out.println("\u001B[31m[ERROR/ChatServer] " + message + ": " + throwable.getMessage());
        new ArrayList<>(
                Arrays.asList(throwable.getStackTrace())
        ).forEach(
                traceLine -> System.out.println("\u001B[31m[ERROR/ChatServer] Stack trace ->" + traceLine + "\u001B[0m")
        );
    }
}
