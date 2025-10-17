package com.metis.models;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private String title;
    private LocalDate dueDate;       // nullable
    private String priority;         // Low | Medium | High
    private boolean completed;

    public Task(String title, LocalDate dueDate, String priority, boolean completed) {
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return (completed ? "[✓] " : "[ ] ") + title +
               (dueDate != null ? " (due " + dueDate + ")" : "") +
               " [" + Objects.toString(priority, "Medium") + "]";
    }
}
