package com.flipfit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/FlipFit";
    private static final String USER = "root"; // Replace with your MySQL 
    private static final String PASS = "Lochan@1999"; // Replace with your MySQL password

    static {
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    	} catch (ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    }
    
    public static Connection getConnection() throws SQLException {
    	return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
