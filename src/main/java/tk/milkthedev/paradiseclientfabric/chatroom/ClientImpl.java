package tk.milkthedev.paradiseclientfabric.chatroom;

import tk.milkthedev.paradiseclientfabric.Constants;
import tk.milkthedev.paradiseclientfabric.Helper;
import tk.milkthedev.paradiseclientfabric.ParadiseClient_Fabric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class ClientImpl implements Runnable
{
    private Socket clientSocket;
    private String host;
    private int port;
    private static ClientImpl clientImpl;
    private BufferedReader in;
    private PrintWriter out;
    private Thread thread;
    public ClientImpl(String host, int port)
    {
        clientImpl = this;
        this.host = host;
        this.port = port;
    }
    @Override
    public void run()
    {
        Helper.printChatMessage("[ChatRoom] Connecting to chat server at " + this.host + ":" + this.port);
        try
        {
            this.clientSocket = new Socket(this.host, this.port);
        } catch (IOException e)
        {
            Constants.LOGGER.info(Arrays.toString(e.getStackTrace()));
            Helper.printChatMessage("[ChatRoom] Unable to connect to the chat server, see logs");
            ParadiseClient_Fabric.getChatRoomMod().isConnected = false;
            return;
        }

        try
        {
            in = new BufferedReader(new java.io.InputStreamReader(this.clientSocket.getInputStream()));
            out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        }
        catch (Exception e)
        {
            Constants.LOGGER.info(Arrays.toString(e.getStackTrace()));
            Helper.printChatMessage("[ChatRoom] An error occurred while creating in and out handlers, see logs");
            ParadiseClient_Fabric.getChatRoomMod().isConnected = false;
            return;
        }
        Helper.printChatMessage("[ChatRoom] Connected successfully");
        ParadiseClient_Fabric.getChatRoomMod().isConnected = true;
        ConnectionHandler connectionHandler = new ConnectionHandler(this.clientSocket, this.in, this.out);
        thread = new Thread(connectionHandler);
        thread.start();
    }
    public void shutdown()
    {
        out.println("[COMMAND]~~~quit");
        if (!clientSocket.isClosed()){try {this.clientSocket.close();} catch (Exception e) {e.printStackTrace();}}
        try
        {
            this.in.close();
            this.out.close();
        }
        catch (Exception e)
        {
            Constants.LOGGER.info(Arrays.toString(e.getStackTrace()));
            Helper.printChatMessage("[ChatRoom] An error occurred while closing in and out handlers, see logs");
        }
        Helper.printChatMessage("[ChatRoom] Disconnected");
        ParadiseClient_Fabric.getChatRoomMod().isConnected = false;
    }
    public void sendMessage(String message)
    {
        out.println(message);
    }
    public static ClientImpl getClientImpl() {return clientImpl;}
}
