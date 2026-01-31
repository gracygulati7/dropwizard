package com.flipfit.dao;

import com.flipfit.bean.FlipFitGymOwner;
import com.flipfit.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OwnerDAO {

    private static OwnerDAO instance = null;

    private OwnerDAO() {}

    public static OwnerDAO getInstance() {
        if (instance == null) {
            synchronized (OwnerDAO.class) {
                if (instance == null) instance = new OwnerDAO();
            }
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DBUtil.getConnection();
    }

    public void addOwnerDetails(int ownerId, String pan, String aadhaar, String gstin) {
        String sql = "INSERT INTO Owner (owner_id, pan, aadhaar, gstin, is_approved) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
            pstmt.setString(2, pan);
            pstmt.setString(3, aadhaar); 
            pstmt.setString(4, gstin);
            pstmt.setInt(5, 0); // 0 = False/Pending
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public FlipFitGymOwner getOwnerById(int id) {
        String sql = "SELECT u.full_name, o.owner_id, o.pan, o.aadhaar, o.gstin, o.is_approved, o.is_validated " +
                "FROM Owner o " +
                "JOIN users u ON o.owner_id = u.user_id " +
                "WHERE o.owner_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    FlipFitGymOwner owner = new FlipFitGymOwner(
                            rs.getInt("owner_id"),
                            rs.getString("full_name"),
                            rs.getString("pan"),
                            rs.getString("aadhaar"),
                            rs.getString("gstin")
                    );
                    owner.setApproved(rs.getInt("is_approved") == 1);
                    owner.setValidated(rs.getInt("is_validated") == 1);
                    return owner;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public FlipFitGymOwner getOwnerByName(String name) {
        String sql = "SELECT u.full_name, o.owner_id, o.pan, o.aadhaar, o.gstin, o.is_approved, o.is_validated " +
                "FROM Owner o " +
                "JOIN users u ON o.owner_id = u.user_id " +
                "WHERE u.full_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    FlipFitGymOwner owner = new FlipFitGymOwner(
                            rs.getInt("owner_id"),
                            rs.getString("full_name"),
                            rs.getString("pan"),
                            rs.getString("aadhaar"),
                            rs.getString("gstin")
                    );
                    owner.setApproved(rs.getInt("is_approved") == 1);
                    owner.setValidated(rs.getInt("is_validated") == 1);
                    return owner;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
    
    
    public FlipFitGymOwner getOwnerByEmail(String email) {
        // FIX: Added is_validated to the query so it can be retrieved during login/viewing
        String sql = "SELECT u.full_name, o.owner_id, o.pan, o.aadhaar, o.gstin, o.is_approved, o.is_validated " +
                     "FROM Owner o " + 
                     "JOIN users u ON o.owner_id = u.user_id " +
                     "WHERE u.email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    FlipFitGymOwner owner = new FlipFitGymOwner(
                            rs.getInt("owner_id"),
                            rs.getString("full_name"),
                            rs.getString("pan"),
                            rs.getString("aadhaar"),
                            rs.getString("gstin")
                    );
                    owner.setApproved(rs.getInt("is_approved") == 1);
                    // FIX: Set the validation status from DB
                    owner.setValidated(rs.getInt("is_validated") == 1);
                    return owner;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public int getUserIdByEmail(String email) {
        String sql = "SELECT user_id FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt("user_id");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }
    

    public void updateOwnerApproval(int ownerId, boolean isApproved) {
        String sql = "UPDATE Owner SET is_approved = ? WHERE owner_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, isApproved ? 1 : 0);
            pstmt.setInt(2, ownerId);
            pstmt.executeUpdate();
            System.out.println("[DB] Owner " + ownerId + " approval status updated to: " + isApproved);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateOwnerValidation(int ownerId, boolean isValidated) {
        String sql = "UPDATE Owner SET is_validated = ? WHERE owner_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, isValidated ? 1 : 0);
            pstmt.setInt(2, ownerId);
            pstmt.executeUpdate();
            System.out.println("[DB] Owner " + ownerId + " validation status updated.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void addOwner(String name) {
        String sql = "INSERT INTO users (full_name, role) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, "OWNER");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public void addOwner(String name, String email, String password) {
        String sql = "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, "OWNER");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public FlipFitGymOwner getOrCreateOwnerByName(String name) {
        FlipFitGymOwner owner = getOwnerByName(name);
        if (owner != null) return owner;
        addOwner(name);
        return getOwnerByName(name);
    }

    public Collection<FlipFitGymOwner> getAllOwners() {
        List<FlipFitGymOwner> owners = new ArrayList<>();
        // FIX: Added is_approved and is_validated to the SQL Query
        String sql = "SELECT u.full_name, o.owner_id, o.pan, o.aadhaar, o.gstin, o.is_approved, o.is_validated " +
                     "FROM Owner o JOIN users u ON o.owner_id = u.user_id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                FlipFitGymOwner owner = new FlipFitGymOwner(
                        rs.getInt("owner_id"),
                        rs.getString("full_name"),
                        rs.getString("pan"),
                        rs.getString("aadhaar"),
                        rs.getString("gstin")
                );
                // FIX: Map boolean results to the owner object
                owner.setApproved(rs.getInt("is_approved") == 1);
                owner.setValidated(rs.getInt("is_validated") == 1);
                owners.add(owner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return owners;
    }

    public int getNextOwnerId() {
        String sql = "SELECT MAX(owner_id) FROM Owner";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (rs.wasNull()) {
                    return 1; 
                }
                return maxId + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 1;
    }
   
}