package com.metis.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

public class MainDashboardController {

    @FXML private StackPane contentRoot;

    @FXML
    private void initialize() {
        // Load a safe default view on start (Calendar if present)
        openCalendar();
    }

    // -------- Navigation handlers --------

    @FXML private void openCalendar()   { loadIntoCenter("CalendarView.fxml"); }
    @FXML private void openToDoList()   { loadIntoCenter("TodoView.fxml"); }
    @FXML private void openNotes()      { loadIntoCenter("NotesView.fxml"); }
    @FXML private void openGPA()        { loadIntoCenter("GpaView.fxml"); }
    @FXML private void openTimetable()  { loadIntoCenter("TimetableView.fxml"); }
    @FXML private void openSettings()   { loadIntoCenter("SettingsView.fxml"); }
    @FXML private void openBackup()     { loadIntoCenter("BackupView.fxml"); }

    // -------- Helper --------

    private void loadIntoCenter(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFileName));
            Parent view = loader.load();
            contentRoot.getChildren().setAll(view);
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Failed to load view");
            a.setContentText("Could not load: " + fxmlFileName + "\n" + ex.getMessage());
            a.showAndWait();
        }
    }
}
