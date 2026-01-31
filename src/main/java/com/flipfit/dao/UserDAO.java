package com.flipfit.dao;

import com.flipfit.bean.FlipFitCustomer;
import com.flipfit.util.DBUtil;
import java.sql.*;

public class UserDAO {
    private static UserDAO instance = null;

    private UserDAO() {}

    public static UserDAO getInstance() {
        if (instance == null) {
            synchronized (UserDAO.class) {
                if (instance == null) instance = new UserDAO();
            }
        }
        return instance;
    }

    public void registerUser(int userId, String name, String email, String password, String role) {
        // Matches your SQL: users (user_id, full_name, email, password, role)
        String sql = "INSERT INTO users (user_id, full_name, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setString(5, role);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Ignore if user already exists during dummy data insertion
            if (!e.getSQLState().equals("23000")) { 
                e.printStackTrace();
            }
        }
    }
    
    
    
    
 // Add these to com.flipfit.dao.UserDAO

    public FlipFitCustomer getUserById(int userId) {
        String sql = "SELECT user_id, full_name, email, role FROM users WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FlipFitCustomer user = new FlipFitCustomer();
                    user.setUserId(rs.getInt("user_id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBaseUser(FlipFitCustomer user) {
        String sql = "UPDATE users SET full_name = ?, email = ? WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating base user: " + e.getMessage());
        }
    }
}