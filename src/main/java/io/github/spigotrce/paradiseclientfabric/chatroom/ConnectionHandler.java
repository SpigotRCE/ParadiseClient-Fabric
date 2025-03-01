package io.github.spigotrce.paradiseclientfabric.chatroom;

import io.github.spigotrce.paradiseclientfabric.Constants;
import io.github.spigotrce.paradiseclientfabric.Helper;
import io.github.spigotrce.paradiseclientfabric.ParadiseClient_Fabric;
import net.minecraft.client.MinecraftClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;

    public ConnectionHandler(Socket client, BufferedReader in, PrintWriter out) {
        this.client = client;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            assert MinecraftClient.getInstance().player != null;
            out.println(MinecraftClient.getInstance().player.getName().getLiteralString());
            String message;
            while ((message = in.readLine()) != null) {
                if (MinecraftClient.getInstance().player == null) {
                    continue;
                }
                Helper.printChatMessage("[ChatRoom] " + message);
            }
        } catch (Exception e) {
            ClientImpl.getClientImpl().shutdown();
            try {
                this.in.close();
                this.out.close();
            } catch (IOException ex) {
                Constants.LOGGER.error("An exception raised while shutting closing reader and writer", e);
                Helper.printChatMessage("[ChatRoom] An exception raised while shutting closing reader and writer, see logs");
                ParadiseClient_Fabric.CHAT_ROOM_MOD.isConnected = false;
            }
        }
    }
}
