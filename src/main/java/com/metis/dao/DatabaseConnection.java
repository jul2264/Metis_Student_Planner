package com.metis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Make sure to change the username and password to your MySQL credentials
    private static final String URL = "jdbc:mysql://localhost:3306/metis_db";
    private static final String USER = "root";
    private static final String PASSWORD = "135790";

    public static Connection getConnection() throws SQLException {
        // This line loads the MySQL driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}