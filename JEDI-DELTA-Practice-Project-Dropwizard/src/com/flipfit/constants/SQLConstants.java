package com.flipfit.constants;

public class SQLConstants {

    // ---------- ADMIN QUERIES ----------
    public static final String ADMIN_LOGIN_QUERY =
        "SELECT u.user_id " +
        "FROM users u " +
        "JOIN admins a ON u.user_id = a.admin_id " +
        "WHERE u.email = ? AND u.password = ? AND u.role = 'ADMIN'";

    // (Later you can add more queries here)
    // public static final String GET_ALL_USERS = "...";
    // public static final String APPROVE_OWNER = "...";
}