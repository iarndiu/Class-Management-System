package com.giggle.server.system;

import com.giggle.bean.Message;
import com.giggle.server.database.Database;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatSystem {
    public static void addMessage(String account, String receiver, String content, int type) {
        var sql = "INSERT INTO message (sender, receiver, message_type, text, message_time) VALUES (?, ?, ?, ?, ?)";
        Object[] params = {
                account,
                receiver,
                type,
                content,
                new Timestamp(Calendar.getInstance().getTimeInMillis())
        };
        Database.getInstance().update(sql, params);
    }

    public static List<Message> getMessageList(String account, String sender, int type) {
        var sql = "";
        if (type == 0) sql = "SELECT * FROM message WHERE ((sender=? AND receiver=?) OR (sender=? AND receiver=?)) AND message_time>? ORDER BY message_time";
        else if (type == 1) sql = "SELECT * FROM message WHERE (message_type=1 AND message_time>?) ORDER BY message_time";
        Object[] params = null;
        if (type == 0) {
            params = new Object[] {
                    account,
                    sender,
                    sender,
                    account,
                    lastGetMessageTime(account, sender, 0)
            };
        } else if (type == 1) {
            params = new Object[] {
                    lastGetMessageTime(account, sender, 1)
            };
        }
        var rs = Database.getInstance().query(sql, params);
        List<Message> list = new ArrayList<>();
        try {
            while (rs.next()) {
                var message = new Message();
                message.sender = rs.getString("sender");
                message.receiver = rs.getString("receiver");
                message.content = rs.getString("text");
                message.messageType = rs.getInt("message_type");
                message.sendTime = LocalDateTime.ofEpochSecond(rs.getTimestamp("message_time").getTime() / 1000, 0, ZoneOffset.ofHours(8));
                list.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateLastGetMessageTime(account, sender, type);
        return list;
    }

    private static void updateLastGetMessageTime(String account, String sender, int type) {
        var sql = "UPDATE message_get_time SET last_get_time=? WHERE account=? AND target=?";
        Object[] params = {
                new Timestamp(Calendar.getInstance().getTimeInMillis()),
                account,
                type == 0 ? sender : "undefined"
        };
        Database.getInstance().update(sql, params);
    }

    private static Timestamp lastGetMessageTime(String account, String sender, int type) {
        var sql = "SELECT last_get_time FROM message_get_time WHERE account=? AND target=?";
        Object[] params = {
                account,
                type == 0 ? sender : "undefined"
        };
        var rs = Database.getInstance().query(sql, params);
        try {
            if (rs.next()) {
                return rs.getTimestamp(1);
            } else {
                var insert = "INSERT INTO message_get_time (account, target, last_get_time) VALUES (?, ?, ?)";
                Object[] params1 = {
                        account,
                        type == 0 ? sender : "undefined",
                        new Timestamp(946656000)
                };
                Database.getInstance().update(insert, params1);
                return new Timestamp(946656000);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Timestamp(946656000);
    }

    public static boolean hasNewMessage(String account, String target, int type) {
        var sql = "";
        if (type == 0) sql = "SELECT * FROM message WHERE ((sender=? AND receiver=?) OR (sender=? AND receiver=?)) AND message_time > ? ORDER BY message_time";
        else if (type == 1) sql = "SELECT * FROM message WHERE message_type=1 AND message_time > ? ORDER BY message_time";
        Object[] params = null;
        if (type == 0) {
            params = new Object[] {
                    account,
                    target,
                    target,
                    account,
                    lastGetMessageTime(account, target, 0)
            };
        } else if (type == 1) {
            params = new Object[] {
                    lastGetMessageTime(account, target, 1)
            };
        }
        var rs = Database.getInstance().query(sql, params);
        try {
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
