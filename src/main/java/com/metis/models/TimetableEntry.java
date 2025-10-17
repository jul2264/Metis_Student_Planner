package com.metis.models;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class TimetableEntry {
    private String course;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;

    public TimetableEntry(String course, DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.course = course;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getCourse() { return course; }
    public DayOfWeek getDay() { return day; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
}

