package com.metis.controllers;

import com.metis.models.TimetableData; // Import the singleton
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TimetableController {

    @FXML private TextField subjectField, timeField, locationField;
    @FXML private ComboBox<String> dayBox;
    @FXML private TableView<ClassEntry> timetableTable;
    @FXML private TableColumn<ClassEntry, String> dayCol, subjectCol, timeCol, locationCol;

    // Get the singleton instance of the class list
    private final ObservableList<ClassEntry> classList = TimetableData.getInstance().getClassList();

    @FXML
    public void initialize() {
        dayBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        dayCol.setCellValueFactory(new PropertyValueFactory<>("day"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        
        // Set the table items to the persistent list from the singleton
        timetableTable.setItems(classList);
    }

    @FXML
    private void addClass() {
        String day = dayBox.getValue();
        String subject = subjectField.getText();
        String time = timeField.getText();
        String location = locationField.getText();

        if (day == null || subject.isEmpty() || time.isEmpty() || location.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        ClassEntry newClass = new ClassEntry(day, subject, time, location);
        TimetableData.getInstance().addClass(newClass); // Add using the singleton
        clearFields();
    }

    @FXML
    private void deleteSelected() {
        ClassEntry selected = timetableTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            TimetableData.getInstance().deleteClass(selected); // Delete using the singleton
        } else {
            showAlert("Please select a class to delete.");
        }
    }

    private void clearFields() {
        subjectField.clear();
        timeField.clear();
        locationField.clear();
        dayBox.setValue(null);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // === Inner class for data model ===
    public static class ClassEntry {
        private final String day, subject, time, location;
        public ClassEntry(String day, String subject, String time, String location) {
            this.day = day; this.subject = subject; this.time = time; this.location = location;
        }
        public String getDay() { return day; }
        public String getSubject() { return subject; }
        public String getTime() { return time; }
        public String getLocation() { return location; }
    }
}