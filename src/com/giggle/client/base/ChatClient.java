package com.giggle.client.base;

import com.giggle.Global;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {
    private boolean isLogin = false;
    public synchronized void login(String account, String password) {
        try {
            outputStream.writeUTF(account);
            outputStream.writeUTF(password);
            var code = inputStream.readInt();
            isLogin = code >= 0;
            if (isLogin) update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChatClient(NewMessageListener messageListener) throws IOException {
        socket = new Socket(Global.HOST, Global.CHAT_PORT);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        this.messageListener = messageListener;
    }

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private NewMessageListener messageListener;

    private void update() {
        new Thread(()-> {
            try {
                // 接收新消息提示
                while (true) {
                    var sender = inputStream.readUTF();
                    var type = inputStream.readInt();
                    if (messageListener != null) {
                        messageListener.receiveMessage(sender, type);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


     //发送消息
     //消息类型，普通消息：0，群消息：1
    public synchronized void sendMessage(String receiver, String message, int type) {
        if (!isLogin) {
            System.out.println("聊天服务器未登录");
            return;
        }
        try {
            outputStream.writeUTF(receiver);
            outputStream.writeUTF(message);
            outputStream.writeInt(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
