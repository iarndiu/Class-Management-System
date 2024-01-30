package com.giggle.server.servers;

import com.giggle.Global;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer extends AbstractServer {

    public ChatServer() {
        try {
            serverSocket = new ServerSocket(Global.CHAT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ClientHandler createClientHandler(Socket socket, AbstractServer server) {
        return new ChatClientHandler(socket, server);
    }

    private Map<String, ChatClientHandler> clientDic = new HashMap<>();

    public void setMapDic(String account, ChatClientHandler clientHandler) {
        clientDic.put(account, clientHandler);
    }

    public void removeMapDic(String account) {
        clientDic.remove(account);
    }

    public void sendTip(String receiver, String account) {
        if (clientDic.get(receiver) != null) clientDic.get(receiver).sendTip(account, 0);
    }

    public void sendGroupTip(String receiver) {
        for (var i : clients) {
            ((ChatClientHandler)i).sendTip(receiver, 1);
        }
    }
}
