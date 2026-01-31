package com.flipfit.dao;

import com.flipfit.bean.FlipFitGymCenter;
import com.flipfit.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GymCentreDAO {

    private static GymCentreDAO instance = null;

    private GymCentreDAO() {}

    public static GymCentreDAO getInstance() {
        if (instance == null) {
            synchronized (GymCentreDAO.class) {
                if (instance == null) instance = new GymCentreDAO();
            }
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DBUtil.getConnection();
    }

    public void addGymCentre(FlipFitGymCenter gymCentre) {
        String sql = "INSERT INTO GymCentreTable (centre_id, owner_id, gym_name, city, state, pincode, capacity, is_approved) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gymCentre.getCenterId());
            pstmt.setInt(2, gymCentre.getOwnerId());
            pstmt.setString(3, gymCentre.getGymName());
            pstmt.setString(4, gymCentre.getCity());
            pstmt.setString(5, gymCentre.getState());
            pstmt.setInt(6, gymCentre.getPincode());
            pstmt.setInt(7, gymCentre.getCapacity());
            pstmt.setBoolean(8, gymCentre.isApproved());

            pstmt.executeUpdate();
            System.out.println("Gym Center added to database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public FlipFitGymCenter addGymCentreAuto(FlipFitGymCenter gymCentre) {
        String sql = "INSERT INTO GymCentreTable (owner_id, gym_name, city, state, pincode, capacity, is_approved) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, gymCentre.getOwnerId());
            pstmt.setString(2, gymCentre.getGymName());
            pstmt.setString(3, gymCentre.getCity());
            pstmt.setString(4, gymCentre.getState());
            pstmt.setInt(5, gymCentre.getPincode());
            pstmt.setInt(6, gymCentre.getCapacity());
            pstmt.setBoolean(7, gymCentre.isApproved());

            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    gymCentre.setCenterId(rs.getInt(1));
                }
            }
            System.out.println("Gym Center added to database successfully.");
            return gymCentre;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<FlipFitGymCenter> getAllCentres() {
        List<FlipFitGymCenter> centers = new ArrayList<>();
        String sql = "SELECT * FROM GymCentreTable";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                FlipFitGymCenter center = new FlipFitGymCenter(
                    rs.getInt("centre_id"),
                    rs.getString("gym_name"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getInt("pincode"),
                    rs.getInt("capacity")
                );
                center.setOwnerId(rs.getInt("owner_id"));
                centers.add(center);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return centers;
    }

    public FlipFitGymCenter getGymCentreById(int centreId) {
        String sql = "SELECT * FROM GymCentreTable WHERE centre_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, centreId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    FlipFitGymCenter center = new FlipFitGymCenter(
                        rs.getInt("centre_id"),
                        rs.getString("gym_name"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getInt("pincode"),
                        rs.getInt("capacity")
                    );
                    center.setOwnerId(rs.getInt("owner_id"));
                    return center;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public void approveCenter(int centerId) {
        String sql = "UPDATE GymCentreTable SET is_approved = 1 WHERE centre_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, centerId);
            pstmt.executeUpdate();
            System.out.println("Center approved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteGymCentre(int centreId) {
        String sql = "DELETE FROM GymCentreTable WHERE centre_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, centreId);
            pstmt.executeUpdate();
            System.out.println("Gym Center deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getNextCentreId() {
        String sql = "SELECT MAX(centre_id) FROM GymCentreTable";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int max = rs.getInt(1);
                if (rs.wasNull() || max == 0) {
                    return 1;
                }
                return max + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 1;
    }

    public boolean centreIdExists(int centreId) {
        String sql = "SELECT 1 FROM GymCentreTable WHERE centre_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, centreId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}