package com.giggle.server.system;

import com.giggle.Global;
import com.giggle.server.database.Database;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClassSystem {
    public static int postAnnouncement(String account, String text) {
        // 普通用户不能发布
        if (UserSystem.userType(account) == 0) {
            return -1;
        }
        var sql = "INSERT INTO announcement (sender, text, post_time) VALUES (?, ?, ?)";
        Object[] params = {
                account,
                text,
                new Timestamp(Calendar.getInstance().getTimeInMillis())
        };
        if (Database.getInstance().update(sql, params)) {
            return 0;
        }
        return -128;
    }

    public static List<Integer> getAnnouncementList() {
        var sql = "SELECT announcement_id from announcement ORDER BY post_time";
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

    public static ArrayList getAnnouncement(int annId) {
        var sql = "SELECT * from announcement WHERE announcement_id=?";
        Object[] params = { annId };
        var rs = Database.getInstance().query(sql, params);
        var list = new ArrayList<>();
        try {
            if (rs.next()) {
                list.add(rs.getString("sender"));
                list.add(rs.getString("text"));
                list.add(rs.getTimestamp("post_time").getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
            list.add("undefined");
            list.add("undefined");
            list.add(0);
        }
        return list;
    }

    public static int postVote(String account, String text) {
        // 普通用户不能发布
        if (UserSystem.userType(account) == 0) {
            return -1;
        }
        var sql = "INSERT INTO vote (sender, text, post_time) VALUES (?, ?, ?)";
        Object[] params = {
                account,
                text,
                new Timestamp(Calendar.getInstance().getTimeInMillis())
        };
        if (Database.getInstance().update(sql, params)) {
            return 0;
        }
        return -128;
    }

    //添加建议
    public static int addAdvice(int voteId,String content){
        var sql = "INSERT INTO vote_advice (vote_id, content) VALUES (?, ?)";
        Object[] params = {
                voteId,
                content,
        };
        if (Database.getInstance().update(sql, params)) {
            return 0;
        }
        return -128;
    }

    //=======================================
    public static String getAdvice(int voteId) {
        var sql = "SELECT content from vote_advice WHERE vote_id=?";
        Object[] params = { voteId };
        var rs = Database.getInstance().query(sql, params);
        String result="advice:\n";
        try {
            while (rs.next()) {
                result+=rs.getString(1);
                result=(result+"\n");//?????
//                return rs.getString(1);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "no advice";
    }

    public static List<Integer> getVoteList() {
        var sql = "SELECT vote_id from vote ORDER BY post_time";
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

    public static boolean isVote(String account, int voteId) {
        var sql = "SELECT * from vote_result WHERE vote_id=? AND vote_account=?";
        Object[] params = {
                voteId,
                account
        };
        var rs = Database.getInstance().query(sql, params);
        try {
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList getVote(String account, int voteId) {
        updateVoteItem(voteId);
        var sql = "SELECT * from vote WHERE vote_id=?";
        Object[] params = { voteId };
        var rs = Database.getInstance().query(sql, params);
        var list = new ArrayList<>();
        try {
            if (rs.next()) {
                list.add(rs.getString("sender"));
                list.add(rs.getString("text"));
                list.add(rs.getTimestamp("post_time").getTime());
                // 状态
                var status = rs.getInt("status");
                if (status == 1) list.add(2);
                else list.add(isVote(account, voteId) ? 1 : 0);
                list.add(rs.getInt("agree"));
                list.add(rs.getInt("disagree"));
            } else {
                list.add("undefined");
                list.add("undefined");
                list.add(0);
                list.add(0);
                list.add(0);
                list.add(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            list.add("undefined");
            list.add("undefined");
            list.add(0);
            list.add(0);
            list.add(0);
            list.add(0);
        }
        return list;
    }

    private static void updateVoteItem(int voteId) {
        var agreeSQL = "SELECT COUNT(*) AS total from vote_result WHERE vote_id=? AND item=0";
        var disagreeSQL = "SELECT COUNT(*) AS total from vote_result WHERE vote_id=? AND item=1";
        Object[] params = { voteId };
        var as = Database.getInstance().query(agreeSQL, params);
        var ds = Database.getInstance().query(disagreeSQL, params);
        int agree = 0;
        int disagree = 0;
        try {
            if (as.next()) {
                agree = as.getInt("total");
            }
            if (ds.next()) {
                disagree = ds.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        var timeSQL = "SELECT post_time from vote WHERE vote_id=?";
        var rs = Database.getInstance().query(timeSQL, new Object[] { voteId });
        long time = 0;
        try {
            if (rs.next()) {
                time = rs.getTimestamp(1).getTime();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        var status = -1;
        if (Calendar.getInstance().getTimeInMillis() - time >= 1000 * 60 * 60 * 24) {
            status = 1;
        }
        var sql = "UPDATE vote SET agree=?, disagree=?, status=? WHERE vote_id=?";
        params = new Object[] {
                agree,
                disagree,
                status == 1 ? 1 : agree + disagree == UserSystem.userCount() - 1 ? 1 : 0,
                voteId
        };
        Database.getInstance().update(sql, params);
    }

    public static int vote(String account, int voteId, int item) {
        if (isVote(account, voteId)) {
            return -1;  // 已经投票
        }
        if ((int)getVote(account, voteId).get(3) == 2) return -2;
        var sql = "INSERT INTO vote_result (vote_id, vote_account, item) VALUES (?, ?, ?)";
        Object[] params = {
                voteId,
                account,
                item
        };
        if (Database.getInstance().update(sql, params)) {
            return 0;
        }
        return -128;
    }

    public static void newDiary(String account,String content){
        var sql = "INSERT INTO diary (account, content,time) VALUES (?, ?, ?)";
        Object[] params = {
                account,
                content,
                new Timestamp(Calendar.getInstance().getTimeInMillis())
        };
//        if (
                Database.getInstance().update(sql, params);
//        ) {
//        }
    }

    public static String getDiary(String account){
        if (UserSystem.userType(account) == 0) {
            return "you're not manager";
        }
        var sql = "SELECT content from diary ";//WHERE vote_id=?";
//        Object[] params = { voteId };
        var rs = Database.getInstance().queryDiary(sql);
        String result="diary:\n";
        try {
            while (rs.next()) {
                result+=rs.getString(1);
                result=(result+"\n");//?????
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "no advice";
    }

//    public static ArrayList getDiary(String account) {
//        var sql = "SELECT * from diary";// WHERE announcement_id=?";
////        Object[] params = { annId };
//        var rs = Database.getInstance().queryDiary(sql);
//        var list = new ArrayList<>();
//        try {
//            if (rs.next()) {
//                list.add(rs.getString("account"));
//                list.add(rs.getString("content"));
//                list.add(rs.getTimestamp("time").getTime());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            list.add("undefined");
//            list.add("undefined");
//            list.add(0);
//        }
//        return list;
//    }
}
