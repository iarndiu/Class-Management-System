package com.giggle.server.servers;

import com.giggle.Global;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractServer {
    protected ServerSocket serverSocket;
    protected List<ClientHandler> clients = new ArrayList<>();

    protected abstract ClientHandler createClientHandler(Socket socket, AbstractServer server);

    public void update() {
        while (true) {
            try {
                var socket = serverSocket.accept();
                var client = createClientHandler(socket, this);
                client.start();
                clients.add(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void remove(ClientHandler client) {
        clients.remove(client);
    }

}
