package io.github.spigotrce.chatroom.shared.network.connection;

import io.github.spigotrce.chatroom.shared.network.packet.Packet;

import java.io.BufferedReader;
import java.io.PrintWriter;

public abstract class AbstractConnection {
    public String username;
    public String version;
    public PrintWriter out;
    public BufferedReader in;
    public abstract void setUsername(String username);
    public abstract void setVersion(String version);
    public abstract void disconnect();
    public abstract void message(String message);
    public abstract void sendPacket(Class<? extends Packet> packet);
    public abstract void receivePacket(byte[] data);
    public abstract void close();
    public abstract boolean isConnected();
}