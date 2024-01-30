package com.giggle.client.base;

import com.giggle.Global;

import java.io.*;
import java.net.Socket;

public class FileClient {
    /**
     * 上传文件
     * @param path 文件路径
     * @param account 上传者
     * @param type 文件类型 0为系统文件 1为群文件
     * @return 上传文件 id
     */
    public static int upload(String path, String account, int type) {
        try {
            var file = new File(path);
            if (!file.exists()) return -1;

            var fin = new FileInputStream(path);
            var socket = new Socket(Global.HOST, Global.FILE_PORT);
            var outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeInt(Global.UPLOAD);
            outputStream.writeUTF(account);
            outputStream.writeUTF(file.getName());
            outputStream.writeLong(file.length());
            outputStream.writeInt(type);
            var code = new DataInputStream(socket.getInputStream()).readInt();
            var bytes = new byte[1024];
            for (int length; (length = fin.read(bytes)) != -1; ) {
                socket.getOutputStream().write(bytes, 0, length);
            }
            socket.shutdownOutput();
            fin.close();
            socket.close();
            return code;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 下载文件
     * @param fileId 要下载的文件id
     * @param path 下载路径，包含文件名
     * @return 下载结果
     */
    public static boolean download(int fileId, String path) {
        try {
            var socket = new Socket(Global.HOST, Global.FILE_PORT);
            var fout = new FileOutputStream(path);
            var outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeInt(Global.DOWNLOAD);
            outputStream.writeInt(fileId);
            var bytes = new byte[1024];
            for (int length; (length = socket.getInputStream().read(bytes)) != -1; ) {
                fout.write(bytes, 0, length);
            }
            fout.close();
            socket.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
