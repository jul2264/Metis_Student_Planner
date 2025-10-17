package com.metis.models;

import com.metis.controllers.CalendarController.Event;
import com.metis.dao.EventDAO;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarData {

    private static final CalendarData instance = new CalendarData();
    private final Map<LocalDate, Event> events;
    private final EventDAO eventDAO = new EventDAO();

    private CalendarData() {
        events = new HashMap<>();
        // Load events from the database
        List<Event> allEvents = eventDAO.getAllEvents();
        for (Event event : allEvents) {
            events.put(event.date, event);
        }
    }

    public static CalendarData getInstance() {
        return instance;
    }

    public Map<LocalDate, Event> getEvents() {
        return events;
    }

    public void addEvent(Event event, LocalDate date) {
        events.put(date, event);
        eventDAO.addEvent(event, date);
    }

    public void deleteEvent(LocalDate date) {
        events.remove(date);
        eventDAO.deleteEvent(date);
    }
}