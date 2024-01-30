package com.giggle.server.system;

import com.giggle.Global;
import com.giggle.server.database.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSystem {
    public static int login(String account, String password) {
        var sql = "SELECT * FROM user WHERE account=? AND password=?";
        Object[] params = {
                account,
                password
        };
        var rs = Database.getInstance().query(sql, params);
        try {
            if (rs.next()) {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    //我改了！！！！！扩展名？？
    public static int register(String account, String username, String password) {
        if (account.equals("undefined") || account.equals("nullptr")) {
            return -2;
        }
        var sql = "INSERT INTO user (account, user_name, password, user_type, avatar_path) VALUES (?, ?, ?, 0, ?)";
        Object[] params = {
                account,
                username,
                password,
                "images/avatars/"+account+".JPG"
        };
        if (Database.getInstance().update(sql, params)) {
            return 0;
        }
        return -1;
    }

    public static String username(String account) {
        var sql = "SELECT user_name from user WHERE account=?";
        Object[] params = { account };
        var rs = Database.getInstance().query(sql, params);
        try {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "undefined";
    }

    public static String password(String account) {
        var sql = "SELECT password from user WHERE account=?";
        Object[] params = { account };
        var rs = Database.getInstance().query(sql, params);
        try {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "undefined";
    }

    //新写的
    public static String avatar(String account) {
        var sql = "SELECT avatar_path from user WHERE account=?";
        Object[] params = { account };
        var rs = Database.getInstance().query(sql, params);
        try {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "undefined";
    }

    public static int userType(String account) {
        var sql = "SELECT user_type from user WHERE account=?";
        Object[] params = { account };
        var rs = Database.getInstance().query(sql, params);
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int setAdmin(String account, String userId, int type) {
        if (userType(account) != -1) {
            return -1;
        }
        var sql = "UPDATE user SET user_type=? WHERE account=?";
        Object[] params = {
                type,
                userId
        };
        if (Database.getInstance().update(sql, params)) {
            return 0;
        }
        return -1;
    }

    public static int userCount() {
        var sql = "SELECT COUNT(*) AS total from user";
        var rs = Database.getInstance().query(sql, null);
        try {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<String> getFriendList(String account) {
        var sql = "SELECT friend_id from friend WHERE account=?";
        Object[] params = { account };
        var rs = Database.getInstance().query(sql, params);
        List<String> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean isFriend(String account, String friendId) {
        var sql = "SELECT * from friend WHERE account=? AND friend_id=?";
        Object[] params = {
                account,
                friendId
        };
        var rs = Database.getInstance().query(sql, params);
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static int addFriend(String account, String friendId) {
        if (isFriend(account, friendId)) {
            return -2;
        }
        var sql = "INSERT INTO friend (account, friend_id) VALUES (?, ?)";
        if (username(friendId).equals("undefined")) {
            return -1;      // 好友账号不存在
        }
        Object[] params0 = {
                account,
                friendId
        };
        Object[] params1 = {
                friendId,
                account
        };
        if (Database.getInstance().update(sql, params0) && Database.getInstance().update(sql, params1)) {
            return 0;
        }
        return -128;
    }

}
