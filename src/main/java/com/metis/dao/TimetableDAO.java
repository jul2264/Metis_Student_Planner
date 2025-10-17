package com.metis.dao;

import com.metis.controllers.TimetableController.ClassEntry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {

    public void addClass(ClassEntry classEntry) {
        String sql = "INSERT INTO timetable(day, subject, time, location) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, classEntry.getDay());
            pstmt.setString(2, classEntry.getSubject());
            pstmt.setString(3, classEntry.getTime());
            pstmt.setString(4, classEntry.getLocation());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<ClassEntry> getAllClasses() {
        String sql = "SELECT * FROM timetable";
        List<ClassEntry> classes = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String day = rs.getString("day");
                String subject = rs.getString("subject");
                String time = rs.getString("time");
                String location = rs.getString("location");
                classes.add(new ClassEntry(day, subject, time, location));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return classes;
    }

    public void deleteClass(String day, String subject, String time, String location) {
        String sql = "DELETE FROM timetable WHERE day = ? AND subject = ? AND time = ? AND location = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, day);
            pstmt.setString(2, subject);
            pstmt.setString(3, time);
            pstmt.setString(4, location);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}