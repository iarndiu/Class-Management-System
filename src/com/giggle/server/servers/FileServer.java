package com.giggle.server.servers;

import com.giggle.Global;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer extends AbstractServer {
    public FileServer() {
        try {
            serverSocket = new ServerSocket(Global.FILE_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected ClientHandler createClientHandler(Socket socket, AbstractServer server) {
        return new FileClientHandler(socket, server);
    }
}
