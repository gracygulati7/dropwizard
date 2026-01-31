package com.flipfit.dao;

import com.flipfit.bean.Slot;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.flipfit.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SlotDAO {

    private static SlotDAO instance = null;

    private SlotDAO() {}

    public static SlotDAO getInstance() {
        if (instance == null) {
            synchronized (SlotDAO.class) {
                if (instance == null) instance = new SlotDAO();
            }
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DBUtil.getConnection();
    }

    public void addSlot(Slot slot) {
        String query = "INSERT INTO slots (slot_id, centre_id, slot_date, start_time, end_time, total_seats, available_seats) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, slot.getSlotId());
            stmt.setInt(2, slot.getCenterId());
            stmt.setDate(3, Date.valueOf(slot.getDate()));
            
            // FIX: Use a helper method to convert string to SQL Time safely
            stmt.setTime(4, convertToSqlTime(slot.getStartTime()));
            stmt.setTime(5, convertToSqlTime(slot.getEndTime())); 
            
            stmt.setInt(6, slot.getTotalSeats());
            stmt.setInt(7, slot.getSeatsAvailable());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    private Time convertToSqlTime(String timeStr) {
        try {
            // This handles H:mm, HH:mm, HH:mm:ss etc.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[H:mm[:ss]][HH:mm[:ss]]");
            LocalTime localTime = LocalTime.parse(timeStr, formatter);
            return Time.valueOf(localTime);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format: " + timeStr + ". Please use HH:mm (e.g. 06:00)");
        }
    }


    public List<Slot> getSlotsByCenterId(int centerId) {
        List<Slot> centerSlots = new ArrayList<>();
        // FIX: centre_id
        String query = "SELECT * FROM slots WHERE centre_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, centerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    centerSlots.add(mapResultSetToSlot(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return centerSlots;
    }

    public Slot getSlotById(int slotId) {
        // FIX: slot_id
        String query = "SELECT * FROM slots WHERE slot_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, slotId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSlot(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public Slot getSlotById(int userId, int slotId, int centerId) {
        // FIX: slot_id, centre_id
        String query = "SELECT * FROM slots WHERE slot_id = ? AND centre_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, slotId);
            stmt.setInt(2, centerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSlot(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
    
    public int getNextSlotId() {
        // FIX: slot_id
        String sql = "SELECT MAX(slot_id) FROM slots";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return (maxId == 0) ? 1 : maxId + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; 
    }

    public List<Slot> getAllSlots() {
        List<Slot> slots = new ArrayList<>();
        String query = "SELECT * FROM slots";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                slots.add(mapResultSetToSlot(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return slots;
    }
    
    public List<Slot> getAvailableSlotsByDateAndCenter(int centerId, LocalDate date) {
        List<Slot> availableSlots = new ArrayList<>();
        // FIX: centre_id, slot_date, available_seats, start_time
        String query = "SELECT * FROM slots WHERE centre_id = ? AND slot_date = ? AND available_seats > 0 AND (slot_date > CURRENT_DATE OR (slot_date = CURRENT_DATE AND start_time > CURRENT_TIME))";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, centerId);
            stmt.setDate(2, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    availableSlots.add(mapResultSetToSlot(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return availableSlots;
    }

    public List<Slot> getFullSlotsByDateAndCenter(int centerId, LocalDate date) {
        List<Slot> fullSlots = new ArrayList<>();
        // FIX: centre_id, slot_date, available_seats
        String query = "SELECT * FROM slots WHERE centre_id = ? AND slot_date = ? AND available_seats = 0";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, centerId);
            stmt.setDate(2, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fullSlots.add(mapResultSetToSlot(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return fullSlots;
    }

    public List<Slot> getExpiredSlots() {
        List<Slot> expiredSlots = new ArrayList<>();
        // FIX: slot_date, start_time
        String query = "SELECT * FROM slots WHERE slot_date < CURRENT_DATE OR (slot_date = CURRENT_DATE AND start_time < CURRENT_TIME)";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                expiredSlots.add(mapResultSetToSlot(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return expiredSlots;
    }

    public List<Slot> getSlotsByDateRange(int centerId, LocalDate startDate, LocalDate endDate) {
        List<Slot> rangeSlots = new ArrayList<>();
        // FIX: centre_id, slot_date
        String query = "SELECT * FROM slots WHERE centre_id = ? AND slot_date BETWEEN ? AND ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, centerId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rangeSlots.add(mapResultSetToSlot(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return rangeSlots;
    }

    private Slot mapResultSetToSlot(ResultSet rs) throws SQLException {
        Slot slot = new Slot();
        
        slot.setSlotId(rs.getInt("slot_id"));
        slot.setCenterId(rs.getInt("centre_id"));
        slot.setDate(rs.getDate("slot_date").toLocalDate());
        
        // Convert SQL Time to String for the bean
        slot.setStartTime(String.valueOf(rs.getTime("start_time").toLocalTime()));
        
        // FIX: Add this line to fetch and set the end_time
        slot.setEndTime(String.valueOf(rs.getTime("end_time").toLocalTime()));
        
        slot.setTotalSeats(rs.getInt("total_seats"));
        slot.setSeatsAvailable(rs.getInt("available_seats"));
        
        return slot;
    }

    public boolean updateSlotSeats(int slotId, int newAvailableSeats) {
        String query = "UPDATE slots SET available_seats = ? WHERE slot_id = ?";
        
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newAvailableSeats);
            stmt.setInt(2, slotId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
