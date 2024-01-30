package com.giggle.server.servers;

import com.giggle.Global;

import java.net.ServerSocket;
import java.net.Socket;

public class PostServer extends AbstractServer {
    public PostServer() {
        try {
            serverSocket = new ServerSocket(Global.INFO_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ClientHandler createClientHandler(Socket socket, AbstractServer server) {
        return new PostClientHandler(socket, server);
    }

}
