package com.flipfit.dao;

import com.flipfit.bean.Booking;
import com.flipfit.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    private static BookingDAO instance = null;

    private BookingDAO() {}

    public static BookingDAO getInstance() {
        if (instance == null) {
            instance = new BookingDAO();
        }
        return instance;
    }

    // Helper method
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setSlotId(rs.getInt("slot_id"));
        booking.setStatus(Booking.BookingStatus.valueOf(rs.getString("status")));
        booking.setDeleted(rs.getBoolean("is_deleted"));
        return booking;
    }

    public Booking createBooking(int userId, int slotId) {
        String sql = "INSERT INTO bookings (user_id, slot_id, status, is_deleted) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setInt(2, slotId);
            ps.setString(3, Booking.BookingStatus.CONFIRMED.name());
            ps.setBoolean(4, false);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return getBookingById(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Booking createWaitlistingBooking(int userId, int slotId) {
        String sql = "INSERT INTO bookings (user_id, slot_id, status, is_deleted) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setInt(2, slotId);
            ps.setString(3, Booking.BookingStatus.WAITLISTED.name());
            ps.setBoolean(4, false);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return getBookingById(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> userBookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? AND is_deleted = false";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userBookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userBookings;
    }

    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cancelBooking(int bookingId) {
        String sql = "UPDATE bookings SET is_deleted = true, status = ? WHERE booking_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, Booking.BookingStatus.CANCELLED.name());
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Booking> getBookingsBySlotId(int slotId) {
        List<Booking> slotBookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE slot_id = ? AND is_deleted = false AND status = 'CONFIRMED'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                slotBookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slotBookings;
    }

    public List<Booking> getAllBookings() {
        List<Booking> allBookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                allBookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allBookings;
    }

    // Deprecated for Database usage as DB handles IDs automatically
    public int getNextBookingId() {
        return 0; 
    }

    // No-op for DB version as we save directly to DB in create methods
    public void addWaitlistedBooking(Booking booking) {
        // Implementation logic is handled in createWaitlistingBooking
    }
}
