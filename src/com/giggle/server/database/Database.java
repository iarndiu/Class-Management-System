package com.giggle.server.database;

import java.sql.*;

public class Database {

    private static final String URL = "jdbc:mysql://localhost:3306/giggle?serverTimezone=GMT%2B8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "bsll2122";
    private static Database instance;

    public static Database getInstance() {
        return instance;
    }

    private Connection conn;

    public Database() {
        instance = this;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setParams(PreparedStatement ps, Object[] params) throws SQLException {
        if (params != null) {
            for (var i = 0; i < params.length; ++i) {
                ps.setObject(i + 1, params[i]);
            }
        }
    }

    public ResultSet query(String sql, Object[] params) {
        try {
            var ps = conn.prepareStatement(sql);
            setParams(ps, params);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQL:" + e.getMessage() + "at query.");
        }
        return null;
    }

    //=================================
    public ResultSet queryDiary(String sql) {
        try {
            var ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQL:" + e.getMessage() + "at query.");
        }
        return null;
    }

    public boolean update(String sql, Object[] params) {
        try {
            var ps = conn.prepareStatement(sql);
            setParams(ps, params);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("SQL:" + e.getMessage() + "at update.");
        }
        return false;
    }

    public int insert(String sql, Object[] params) {
        try {
            var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParams(ps, params);
            var code = ps.executeUpdate();
            if (code == 0) return -1;
            var rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("SQL:" + e.getMessage() + "at insert.");
        }
        return -1;
    }
}
