package com.giggle.server;

import com.giggle.server.database.Database;
import com.giggle.server.servers.ChatServer;
import com.giggle.server.servers.FileServer;
import com.giggle.server.servers.PostServer;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) {
        new ServerMain();
    }

    ServerMain() {
        new Database();
        System.out.println("数据库加载完成");
        new Thread(()-> new PostServer().update()).start();
        System.out.println("Post服务器加载完成");
        new Thread(()-> new FileServer().update()).start();
        System.out.println("File服务器加载完成");
        new Thread(()-> new ChatServer().update()).start();
        System.out.println("Chat服务器加载完成");
    }
}
