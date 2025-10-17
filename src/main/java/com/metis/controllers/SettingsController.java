package com.metis.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

public class SettingsController {

    @FXML private ChoiceBox<String> themeChoiceBox;
    @FXML private ChoiceBox<String> fontChoiceBox;
    @FXML private CheckBox notificationsCheckBox;

    @FXML
    private void initialize() {
        System.out.println("✅ SettingsController initialized");

        // Populate dropdowns manually (no <String> tags needed)
        themeChoiceBox.setItems(FXCollections.observableArrayList("Light", "Dark"));
        fontChoiceBox.setItems(FXCollections.observableArrayList("Small", "Medium", "Large"));

        // Default selections
        themeChoiceBox.setValue("Dark");
        fontChoiceBox.setValue("Medium");
        notificationsCheckBox.setSelected(true);
    }

    @FXML
    private void onBackup() {
        showAlert("Backup Complete", "Your data has been backed up successfully.");
    }

    @FXML
    private void onRestore() {
        showAlert("Restore Complete", "Your data has been restored successfully.");
    }

    @FXML
    private void onReset() {
        themeChoiceBox.setValue("Dark");
        fontChoiceBox.setValue("Medium");
        notificationsCheckBox.setSelected(true);
        showAlert("Reset Complete", "Settings have been reset to default.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
