package com.giggle.server.system;

import com.giggle.server.database.Database;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FileSystem {

    public static int addFile(String account, String fileName, int type, long fileLength) {
        var sql = "INSERT INTO file (file_name, length, uploader, type, upload_time) VALUES (?, ?, ?, ?, ?)";
        Object[] params = {
                fileName,
                fileLength,
                account,
                type,
                new Timestamp(Calendar.getInstance().getTimeInMillis())
        };
        return Database.getInstance().insert(sql, params);
    }

    public static ArrayList getFileInfo(int fileId) {
        var sql = "SELECT * FROM file WHERE file_id=?";
        Object[] params = {
                fileId
        };
        var rs = Database.getInstance().query(sql, params);
        var list = new ArrayList<>();
        try {
            if (rs.next()) {
                list.add(rs.getString("uploader"));
                list.add(rs.getString("file_name"));
                list.add(rs.getLong("length"));
                list.add(rs.getTimestamp("upload_time").getTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Integer> getFileList() {
        var sql = "SELECT file_id from file WHERE type=1 ORDER BY upload_time";
        var rs = Database.getInstance().query(sql, null);
        List<Integer> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(rs.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
