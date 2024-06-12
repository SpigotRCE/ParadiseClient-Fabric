package tk.milkthedev.paradiseclientfabric.chatroom;

import net.minecraft.client.MinecraftClient;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable
{
    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;

    public ConnectionHandler(Socket client, BufferedReader in, PrintWriter out)
    {
        this.client = client;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run()
    {
        try
        {
            out.println(MinecraftClient.getInstance().player.getName().getLiteralString());
            String message;
            while ((message = in.readLine())!= null)
            {
                Helper.printChatMessage("[ChatRoom] " + message);
            }
        }
        catch (Exception e)
        {
            ClientImpl.getClientImpl().shutdown();
            try
            {
                this.in.close();
                this.out.close();
            } catch (IOException ex)
            {
                Helper.printChatMessage("[ChatRoom] An error occurred while closing the connection to the chat server. See logs");
                ParadiseClient_Fabric.getChatRoomMod().isConnected = false;
            }
        }
    }
}
