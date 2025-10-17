package com.metis.dao;

import com.metis.controllers.CalendarController.Event;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public void addEvent(Event event, LocalDate date) {
        String sql = "INSERT INTO events(title, time, description, event_date) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.title);
            pstmt.setString(2, event.time);
            pstmt.setString(3, event.description);
            pstmt.setDate(4, Date.valueOf(date));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Event> getAllEvents() {
        String sql = "SELECT * FROM events";
        List<Event> events = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String title = rs.getString("title");
                String time = rs.getString("time");
                String description = rs.getString("description");
                LocalDate eventDate = rs.getDate("event_date").toLocalDate();
                Event event = new Event(title, time, description);
                event.date = eventDate;
                events.add(event);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return events;
    }

    public void deleteEvent(LocalDate date) {
        String sql = "DELETE FROM events WHERE event_date = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}