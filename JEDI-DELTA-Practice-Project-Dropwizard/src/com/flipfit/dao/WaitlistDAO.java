package com.flipfit.dao;

import com.flipfit.util.DBUtil;
import java.sql.*;

/**
 * Data Access Object (DAO) for managing the gym waitlist system. Handles
 * database operations for adding, removing, and checking waitlist status.
 */
public class WaitlistDAO {

	/** Single volatile instance for thread-safe Singleton pattern */
	private static volatile WaitlistDAO instance = null;

	/**
	 * Private constructor to prevent external instantiation.
	 */
	private WaitlistDAO() {
	}

	/**
	 * Returns a thread-safe Singleton instance of WaitlistDAO using double-checked
	 * locking. * @return The Singleton instance of WaitlistDAO
	 */
	public static WaitlistDAO getInstance() {
		if (instance == null) {
			synchronized (WaitlistDAO.class) {
				if (instance == null)
					instance = new WaitlistDAO();
			}
		}
		return instance;
	}

	/**
	 * Helper method to establish a connection to the MySQL database via DBUtil.
	 * * @return Active Connection object
	 * 
	 * @throws SQLException If database access fails
	 */
	private Connection getConnection() throws SQLException {
		return DBUtil.getConnection();
	}

	/**
	 * Adds a user to the waitlist for a specific gym slot. * @param slotId The ID
	 * of the gym slot
	 * 
	 * @param userId The ID of the user joining the waitlist
	 * @throws RuntimeException if a database error occurs
	 */
	public void addToWaitlist(int slotId, int userId) {
		// Updated to match SQL schema column names: slot_id, user_id
		String query = "INSERT INTO waitlist (slot_id, user_id) VALUES (?, ?)";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, slotId);
			stmt.setInt(2, userId);
			stmt.executeUpdate();
			System.out.println("[DB] User " + userId + " added to waitlist for slot " + slotId);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Removes the oldest waitlisted user (FIFO) for a slot and returns their
	 * userId. Utilizes a database transaction and SELECT FOR UPDATE to prevent race
	 * conditions between multiple threads trying to promote the same user. * @param
	 * slotId The ID of the gym slot
	 * 
	 * @return The userId of the promoted user, or null if the waitlist is empty
	 * @throws RuntimeException if a database error occurs during the transaction
	 */
	public Integer removeFromWaitlist(int slotId) {
		// Updated column names: waitlist_id, user_id, slot_id
		String selectQuery = "SELECT waitlist_id, user_id FROM waitlist WHERE slot_id = ? ORDER BY waitlist_id ASC LIMIT 1 FOR UPDATE";
		String deleteQuery = "DELETE FROM waitlist WHERE waitlist_id = ?";

		try (Connection conn = getConnection()) {
			try {
				// Begin Transaction
				conn.setAutoCommit(false);

				try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
					selectStmt.setInt(1, slotId);
					try (ResultSet rs = selectStmt.executeQuery()) {
						if (rs.next()) {
							// Updated to match SQL snake_case column names
							int waitlistId = rs.getInt("waitlist_id");
							int userId = rs.getInt("user_id");

							// Delete the specific entry by unique ID
							try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
								deleteStmt.setInt(1, waitlistId);
								deleteStmt.executeUpdate();
							}

							// Finalize changes
							conn.commit();
							return userId;
						} else {
							// No one in line, safely rollback and return null
							conn.rollback();
							return null;
						}
					}
				}
			} catch (SQLException ex) {
				// Rollback in case of any SQL exceptions during the transaction
				try {
					conn.rollback();
				} catch (SQLException rollbackEx) {
					rollbackEx.printStackTrace();
				}
				ex.printStackTrace();
				throw new RuntimeException(ex);
			} finally {
				// Restore default auto-commit behavior
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Checks if any users are currently waiting for a specific slot. * @param
	 * slotId The ID of the gym slot
	 * 
	 * @return true if the waitlist contains at least one entry, false otherwise
	 */
	public boolean hasWaitlistedCustomers(int slotId) {
		// Updated to match SQL schema column name: slot_id
		String query = "SELECT COUNT(*) FROM waitlist WHERE slot_id = ?";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, slotId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return false;
	}

	/**
	 * Calculates the total number of users waiting for a specific slot. * @param
	 * slotId The ID of the gym slot
	 * 
	 * @return The count of entries in the waitlist for that slot
	 */
	public int getWaitlistSize(int slotId) {
		// Updated to match SQL schema column name: slot_id
		String query = "SELECT COUNT(*) FROM waitlist WHERE slot_id = ?";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, slotId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return 0;
	}
}