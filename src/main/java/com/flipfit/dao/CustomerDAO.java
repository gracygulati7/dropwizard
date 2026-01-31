package com.flipfit.dao;

import com.flipfit.bean.FlipFitCustomer;
import com.flipfit.util.DBUtil;

import java.sql.*;
import java.util.*;

public class CustomerDAO {
    private static CustomerDAO instance = null;

    private CustomerDAO() {}

    public static CustomerDAO getInstance() {
        if (instance == null) {
            synchronized (CustomerDAO.class) {
                if (instance == null) instance = new CustomerDAO();
            }
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DBUtil.getConnection();
    }

    // UPDATED: Now accepts email and password from the Menu instead of hardcoding them
    public FlipFitCustomer addCustomer(String fullName, String email, String password) {
        String userSql = "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, ?)";
        String customerSql = "INSERT INTO customers (customer_id, full_name, role) VALUES (?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); 

            int newId = -1;
            try (PreparedStatement psUser = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, fullName);
                psUser.setString(2, email);    // Using actual email
                psUser.setString(3, password); // Using actual password
                psUser.setString(4, "CUSTOMER");
                psUser.executeUpdate();

                ResultSet rs = psUser.getGeneratedKeys();
                if (rs.next()) newId = rs.getInt(1);
            }

            if (newId != -1) {
                try (PreparedStatement psCust = conn.prepareStatement(customerSql)) {
                    psCust.setInt(1, newId); 
                    psCust.setString(2, fullName);
                    psCust.setString(3, "CUSTOMER");
                    psCust.executeUpdate();
                }
            }

            conn.commit(); 

            FlipFitCustomer c = new FlipFitCustomer();
            c.setUserId(newId);
            c.setFullName(fullName);
            return c;

        } catch (SQLException e) {
            throw new RuntimeException("Error during customer registration: " + e.getMessage());
        }
    }

    // NEW: Added to fix the "account not found" error during login
    public FlipFitCustomer getCustomerByEmail(String email) {
        // We join with users table because that's where the email is stored
        String sql = "SELECT c.* FROM customers c JOIN users u ON c.customer_id = u.user_id WHERE u.email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToCustomer(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public FlipFitCustomer getCustomerByName(String name) {
        String sql = "SELECT * FROM customers WHERE LOWER(full_name) = LOWER(?) LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToCustomer(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Keep this for compatibility, but you should use addCustomer(name, email, pass)
    public FlipFitCustomer addCustomer(String fullName) {
        return addCustomer(fullName, fullName.toLowerCase().replace(" ", "") + "@flipfit.com", "password123");
    }

    public FlipFitCustomer getOrCreateCustomerByName(String name) {
        FlipFitCustomer c = getCustomerByName(name);
        if (c != null) return c;
        return addCustomer(name);
    }

    public FlipFitCustomer getCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE customer_id = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToCustomer(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<FlipFitCustomer> getAllCustomers() {
        List<FlipFitCustomer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToCustomer(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCustomer(FlipFitCustomer user) {
        if (user == null) return;
        String sql = "UPDATE customers SET full_name = ?, role = ?, contact = ?, payment_type = ?, payment_info = ? WHERE customer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getRole());
            ps.setString(3, user.getContact());

            if (user.getPaymentType() != 0) ps.setInt(4, user.getPaymentType());
            else ps.setNull(4, Types.INTEGER);

            ps.setString(5, user.getPaymentInfo());
            ps.setInt(6, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePaymentDetails(int userId, int paymentType, String paymentInfo) {
        String sql = "UPDATE customers SET payment_type = ?, payment_info = ? WHERE customer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentType);
            ps.setString(2, paymentInfo);
            ps.setInt(3, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private FlipFitCustomer mapRowToCustomer(ResultSet rs) throws SQLException {
        FlipFitCustomer c = new FlipFitCustomer();
        c.setUserId(rs.getInt("customer_id"));
        c.setFullName(rs.getString("full_name"));
        c.setRole(rs.getString("role"));
        c.setContact(rs.getString("contact"));
        int pt = rs.getInt("payment_type");
        if (!rs.wasNull()) c.setPaymentType(pt);
        c.setPaymentInfo(rs.getString("payment_info"));
        return c;
    }
}