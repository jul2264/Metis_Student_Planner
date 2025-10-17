package com.metis.controllers;

import com.metis.models.CalendarData; // Import the singleton
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

public class CalendarController {

    @FXML private Label monthLabel;
    @FXML private GridPane calendarGrid;
    @FXML private AnchorPane eventPopup;
    @FXML private TextField titleField;
    @FXML private ComboBox<String> timePicker;
    @FXML private TextArea descField;
    @FXML private Button deleteButton;

    private YearMonth currentMonth;
    private LocalDate selectedDate;

    // Get the singleton instance
    private final Map<LocalDate, Event> events = CalendarData.getInstance().getEvents();

    @FXML
    public void initialize() {
        currentMonth = YearMonth.now();
        setupTimePicker();
        drawCalendar(currentMonth);
        titleField.setStyle("-fx-text-fill: black;");
        descField.setStyle("-fx-text-fill: white;");
    }

    private void drawCalendar(YearMonth month) {
        calendarGrid.getChildren().clear();
        monthLabel.setText(month.getMonth().name() + " " + month.getYear());

        LocalDate firstDay = month.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue();
        int daysInMonth = month.lengthOfMonth();
        int col = dayOfWeek - 1;
        int row = 0;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = month.atDay(day);
            VBox cell = new VBox();
            cell.setPrefSize(100, 80);
            cell.setStyle("-fx-background-color: white; -fx-border-color: #94A3B8; -fx-padding: 5;");
            cell.setSpacing(5);

            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setStyle("-fx-font-weight: bold;-fx-text-fill: black;-fx-font-size: 14px;-fx-alignment: center;-fx-padding: 4;");

            if (events.containsKey(date)) {
                Event e = events.get(date);
                Label eventLabel = new Label("• " + e.title + " (" + e.time + ")");
                eventLabel.setStyle("-fx-text-fill: #6366F1; -fx-font-size: 11;");
                cell.getChildren().addAll(dayLabel, eventLabel);
            } else {
                cell.getChildren().add(dayLabel);
            }

            cell.setOnMouseClicked(evt -> openEventPopup(date));
            calendarGrid.add(cell, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private void openEventPopup(LocalDate date) {
        selectedDate = date;
        if (events.containsKey(date)) {
            Event e = events.get(date);
            titleField.setText(e.title);
            descField.setText(e.description);
            timePicker.setValue(e.time);
            deleteButton.setVisible(true);
        } else {
            titleField.clear();
            descField.clear();
            timePicker.setValue(null);
            deleteButton.setVisible(false);
        }
        eventPopup.setVisible(true);
    }

    @FXML
    private void saveEvent() {
        if (selectedDate != null && !titleField.getText().isEmpty()) {
            String selectedTime = timePicker.getValue() != null ? timePicker.getValue() : "All day";
            Event newEvent = new Event(titleField.getText(), selectedTime, descField.getText());
            CalendarData.getInstance().addEvent(newEvent, selectedDate); // Add using singleton
            eventPopup.setVisible(false);
            drawCalendar(currentMonth);
        }
    }

    @FXML
    private void deleteEvent() {
        if (selectedDate != null && events.containsKey(selectedDate)) {
            CalendarData.getInstance().deleteEvent(selectedDate); // Delete using singleton
            eventPopup.setVisible(false);
            drawCalendar(currentMonth);
        }
    }

    @FXML private void cancelEvent() { eventPopup.setVisible(false); }
    @FXML private void goToToday() { currentMonth = YearMonth.now(); drawCalendar(currentMonth); }
    @FXML private void goToPreviousMonth() { currentMonth = currentMonth.minusMonths(1); drawCalendar(currentMonth); }
    @FXML private void goToNextMonth() { currentMonth = currentMonth.plusMonths(1); drawCalendar(currentMonth); }

    private void setupTimePicker() {
        for (int hour = 0; hour < 24; hour++) {
            String ampm = (hour < 12) ? "a.m." : "p.m.";
            int displayHour = (hour == 0 || hour == 12) ? 12 : hour % 12;
            timePicker.getItems().add(displayHour + ":00 " + ampm);
            timePicker.getItems().add(displayHour + ":30 " + ampm);
        }
    }

    // Inner Event Class
    public static class Event {
        public String title;
        public String time;
        public String description;
        public LocalDate date;

        public Event(String title, String time, String description) {
            this.title = title;
            this.time = time;
            this.description = description;
        }
    }
}