package com.metis.dao;

import com.metis.models.Task;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public void addTask(Task task) {
        String sql = "INSERT INTO tasks(title, dueDate, priority, completed) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            if (task.getDueDate() != null) {
                pstmt.setDate(2, Date.valueOf(task.getDueDate()));
            } else {
                pstmt.setNull(2, Types.DATE);
            }
            pstmt.setString(3, task.getPriority());
            pstmt.setBoolean(4, task.isCompleted());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String title = rs.getString("title");
                LocalDate dueDate = rs.getDate("dueDate") != null ? rs.getDate("dueDate").toLocalDate() : null;
                String priority = rs.getString("priority");
                boolean completed = rs.getBoolean("completed");
                tasks.add(new Task(title, dueDate, priority, completed));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tasks;
    }

    public void updateTask(Task task) {
        String sql = "UPDATE tasks SET dueDate = ?, priority = ?, completed = ? WHERE title = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (task.getDueDate() != null) {
                pstmt.setDate(1, Date.valueOf(task.getDueDate()));
            } else {
                pstmt.setNull(1, Types.DATE);
            }
            pstmt.setString(2, task.getPriority());
            pstmt.setBoolean(3, task.isCompleted());
            pstmt.setString(4, task.getTitle());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteTask(String title) {
        String sql = "DELETE FROM tasks WHERE title = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}