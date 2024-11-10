package io.github.spigotrce.chatroom.shared.env;

import java.io.BufferedReader;
import java.io.PrintWriter;

public abstract class AbstractEnvironment {
    public boolean isConnected;
    public PrintWriter out;
    public BufferedReader in;
}
