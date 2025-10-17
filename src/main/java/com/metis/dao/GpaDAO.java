package com.metis.dao;

import com.metis.controllers.GpaController.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GpaDAO {

    public void addCourse(Course course) {
        String sql = "INSERT INTO courses(subject, credits, grade, gradePoints) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getSubject());
            pstmt.setInt(2, course.getCredits());
            pstmt.setString(3, course.getGrade());
            pstmt.setDouble(4, course.getGradePoints());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Course> getAllCourses() {
        String sql = "SELECT * FROM courses";
        List<Course> courses = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String subject = rs.getString("subject");
                int credits = rs.getInt("credits");
                String grade = rs.getString("grade");
                double gradePoints = rs.getDouble("gradePoints");
                courses.add(new Course(subject, credits, grade, gradePoints));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return courses;
    }

    public void deleteCourse(String subject) {
        String sql = "DELETE FROM courses WHERE subject = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}