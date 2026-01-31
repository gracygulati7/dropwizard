package com.flipfit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.flipfit.util.DBUtil;
import com.flipfit.constants.SQLConstants;

public class AdminDAO {

    private static AdminDAO instance;

    private AdminDAO() {}

    public static AdminDAO getInstance() {
        if (instance == null) {
            instance = new AdminDAO();
        }
        return instance;
    }

    // ---- ADMIN AUTHENTICATION ----
    public boolean login(String username, String password) {

    	// System.out.println("[Debug] Attempting login for Email: " + username + " with Role: ADMIN");
    	
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps =
                 conn.prepareStatement(SQLConstants.ADMIN_LOGIN_QUERY)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();   // true → valid admin

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}