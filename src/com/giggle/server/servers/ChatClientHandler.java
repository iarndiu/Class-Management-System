package com.giggle.server.servers;

import com.giggle.server.system.ChatSystem;
import com.giggle.server.system.UserSystem;

import java.io.IOException;
import java.net.Socket;

public class ChatClientHandler extends ClientHandler {
    private String account;
    ChatClientHandler(Socket socket, AbstractServer server) {
        super(socket, server);
    }

    @Override
    public void run() {
        try {
            while (true) {
                var account = inputStream.readUTF();
                var password = inputStream.readUTF();
                if (UserSystem.login(account, password) >= 0) {
                    // 登录成功
                    this.account = account;
                    ((ChatServer)server).setMapDic(account, this);
                    outputStream.writeInt(0);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取用户发送的消息
        new Thread(()-> {
            try {
                while (true) {
                    var receiver = inputStream.readUTF();
                    var content = inputStream.readUTF();
                    var type = inputStream.readInt();
                    if (type != 0 && type != 1) {
                        continue;
                    }
                    ChatSystem.addMessage(account, receiver, content, type);
                    if (type == 0) {
                        ((ChatServer) server).sendTip(receiver, account);
                        ((ChatServer) server).sendTip(account, receiver);
                    }
                    else ((ChatServer) server).sendGroupTip(receiver);
                }
            } catch (IOException e) {
                server.remove(this);
                ((ChatServer)server).removeMapDic(account);
            }
        }).start();
    }

    public void sendTip(String sender, int type) {
        try {
            outputStream.writeUTF(sender);
            outputStream.writeInt(type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
