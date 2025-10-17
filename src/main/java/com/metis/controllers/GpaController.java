package com.metis.controllers;

import com.metis.models.GpaData; // Import the singleton
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class GpaController {

    @FXML private TextField subjectField;
    @FXML private TextField creditsField;
    @FXML private ComboBox<String> gradeBox;
    @FXML private TableView<Course> gpaTable;
    @FXML private TableColumn<Course, String> subjectCol;
    @FXML private TableColumn<Course, Integer> creditsCol;
    @FXML private TableColumn<Course, String> gradeCol;
    @FXML private TableColumn<Course, Double> pointsCol;
    @FXML private Label gpaResultLabel;

    // Get the singleton instance
    private final ObservableList<Course> courses = GpaData.getInstance().getCourses();

    @FXML
    public void initialize() {
        gradeBox.setItems(FXCollections.observableArrayList("O", "A+", "A", "B+", "B", "C+", "C", "F"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        creditsCol.setCellValueFactory(new PropertyValueFactory<>("credits"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("gradePoints"));

        gpaTable.setItems(courses);
        updateGpa();
    }

    @FXML
    private void addCourse() {
        try {
            String subject = subjectField.getText();
            String grade = gradeBox.getValue();
            int credits = Integer.parseInt(creditsField.getText());

            if (subject.isEmpty() || grade == null) {
                showAlert("Please fill in all fields.");
                return;
            }

            double points = gradeToPoints(grade);
            Course newCourse = new Course(subject, credits, grade, points);
            GpaData.getInstance().addCourse(newCourse); // Add using the singleton
            updateGpa();

            subjectField.clear();
            creditsField.clear();
            gradeBox.getSelectionModel().clearSelection();
        } catch (NumberFormatException e) {
            showAlert("Credits must be a valid number.");
        }
    }

    @FXML
    private void deleteSelected() {
        Course selected = gpaTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            GpaData.getInstance().deleteCourse(selected); // Delete using the singleton
            updateGpa();
        } else {
            showAlert("Please select a course to delete.");
        }
    }

    private void updateGpa() {
        double totalPoints = 0;
        int totalCredits = 0;
        for (Course c : courses) {
            totalPoints += c.getCredits() * c.getGradePoints();
            totalCredits += c.getCredits();
        }
        double gpa = totalCredits == 0 ? 0 : totalPoints / totalCredits;
        gpaResultLabel.setText(String.format("%.2f / 10", gpa));
    }

    private double gradeToPoints(String grade) {
        return switch (grade) {
            case "O" -> 10;
            case "A+" -> 9;
            case "A" -> 8;
            case "B+" -> 7;
            case "B" -> 6;
            case "C+" -> 5;
            case "C" -> 4;
            default -> 0;
        };
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class for TableView
    public static class Course {
        private final String subject;
        private final int credits;
        private final String grade;
        private final double gradePoints;

        public Course(String subject, int credits, String grade, double gradePoints) {
            this.subject = subject;
            this.credits = credits;
            this.grade = grade;
            this.gradePoints = gradePoints;
        }

        public String getSubject() { return subject; }
        public int getCredits() { return credits; }
        public String getGrade() { return grade; }
        public double getGradePoints() { return gradePoints; }
    }
}