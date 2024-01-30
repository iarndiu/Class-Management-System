package com.giggle.server.servers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    protected Socket socket;
    protected AbstractServer server;

    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;
    ClientHandler(Socket socket, AbstractServer server) {
        this.socket = socket;
        this.server = server;
        try {
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
