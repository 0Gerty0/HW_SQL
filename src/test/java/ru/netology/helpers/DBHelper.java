package ru.netology.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "app_pass";

    public static String getLatestAuthCode(String login) {
        String authCode = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT auth_codes.code FROM auth_codes " +
                             "JOIN users ON auth_codes.user_id = users.id " +
                             "WHERE users.login = '" + login + "' ORDER BY auth_codes.created DESC LIMIT 1;")) {
            if (rs.next()) {
                authCode = rs.getString("code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authCode;
    }
}
