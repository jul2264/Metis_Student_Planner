package com.metis.models;

import java.time.LocalDateTime;

public class Event {
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String location;

    public Event(String title, LocalDateTime start, LocalDateTime end, String location) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.location = location;
    }

    public String getTitle() { return title; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public String getLocation() { return location; }
}

