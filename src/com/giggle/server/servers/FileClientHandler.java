package com.giggle.server.servers;

import com.giggle.Global;
import com.giggle.server.system.FileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class FileClientHandler extends ClientHandler{
    FileClientHandler(Socket socket, AbstractServer server) {
        super(socket, server);
    }

    @Override
    public void run() {
        try {
            var code = inputStream.readInt();
            if (code == Global.UPLOAD) {
                // 上传文件
                var account = inputStream.readUTF();
                var fileName = inputStream.readUTF();
                var fileLength = inputStream.readLong();
                var fileType = inputStream.readInt();
                var fileId = FileSystem.addFile(account, fileName, fileType, fileLength);
                outputStream.writeInt(fileId);
                var path = Global.SERVER_FILE_SAVE_PATH;

                if (!new File(path).exists() || !new File(path).isDirectory()) {
                    new File(path).mkdirs();
                }
                var fout = new FileOutputStream(path + "/" + fileId);
                var bytes = new byte[1024];
                for (int length; (length = socket.getInputStream().read(bytes)) != -1; ) {
                    fout.write(bytes, 0, length);
                }
                fout.close();
            } else if (code == Global.DOWNLOAD) {
                var fileId = inputStream.readInt();
                var path = Global.SERVER_FILE_SAVE_PATH;
                var fin = new FileInputStream(path + "/" + fileId);
                var bytes = new byte[1024];
                for (int length; (length = fin.read(bytes)) != -1; ) {
                    socket.getOutputStream().write(bytes, 0, length);
                }
                socket.shutdownOutput();
                fin.close();
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        server.remove(this);
    }
}
