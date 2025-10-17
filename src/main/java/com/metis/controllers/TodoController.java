package com.metis.controllers;

import com.metis.models.Task;
import com.metis.models.TaskData; // Import the singleton
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Objects;

public class TodoController {

    @FXML private TextField taskInput;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<String> priorityBox;
    @FXML private Button addBtn;

    @FXML private TextField searchField;
    @FXML private ChoiceBox<String> statusFilter;

    @FXML private ListView<Task> taskList;
    @FXML private Button toggleBtn;
    @FXML private Button deleteBtn;

    // Get the singleton instance of the task list
    private final ObservableList<Task> master = TaskData.getInstance().getTasks();

    @FXML
    private void initialize() {
        // Priority options
        priorityBox.setItems(FXCollections.observableArrayList("Low", "Medium", "High"));
        priorityBox.getSelectionModel().select("Medium");

        // Status filter
        statusFilter.setItems(FXCollections.observableArrayList("All", "Active", "Completed"));
        statusFilter.getSelectionModel().select("All");

        // Filtering
        FilteredList<Task> filtered = new FilteredList<>(master, t -> true);
        searchField.textProperty().addListener((obs, o, n) -> applyFilter(filtered));
        statusFilter.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> applyFilter(filtered));
        applyFilter(filtered);

        SortedList<Task> sorted = new SortedList<>(filtered);
        taskList.setItems(sorted);

        // Custom cells
        taskList.setCellFactory(makeCellFactory());

        // Buttons
        addBtn.setOnAction(e -> onAdd());
        toggleBtn.setOnAction(e -> onToggle());
        deleteBtn.setOnAction(e -> onDelete());
    }

    private void applyFilter(FilteredList<Task> filtered) {
        final String q = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        final String status = statusFilter.getSelectionModel().getSelectedItem();

        filtered.setPredicate(t -> {
            if (t == null) return false;
            boolean matchesText = q.isEmpty() || t.getTitle().toLowerCase().contains(q);
            boolean matchesStatus = "All".equals(status) ||
                    ("Active".equals(status) && !t.isCompleted()) ||
                    ("Completed".equals(status) && t.isCompleted());
            return matchesText && matchesStatus;
        });
    }

    private void onAdd() {
        String title = taskInput.getText();
        if (title == null || title.trim().isEmpty()) {
            showAlert("Enter a task title.");
            return;
        }
        LocalDate due = dueDatePicker.getValue();
        String priority = priorityBox.getValue() == null ? "Medium" : priorityBox.getValue();

        Task newTask = new Task(title.trim(), due, priority, false);
        TaskData.getInstance().addTask(newTask); // Add using the singleton

        taskInput.clear();
        dueDatePicker.setValue(null);
        priorityBox.getSelectionModel().select("Medium");
    }

    private void onToggle() {
        Task t = taskList.getSelectionModel().getSelectedItem();
        if (t == null) return;
        t.setCompleted(!t.isCompleted());
        TaskData.getInstance().updateTask(t); // Update using the singleton
        taskList.refresh();
    }

    private void onDelete() {
        Task t = taskList.getSelectionModel().getSelectedItem();
        if (t != null) {
            TaskData.getInstance().deleteTask(t); // Delete using the singleton
        }
    }

    private Callback<ListView<Task>, ListCell<Task>> makeCellFactory() {
        return lv -> new ListCell<>() {
            private final CheckBox box = new CheckBox();
            private final Label title = new Label();
            private final Label dueChip = new Label();
            private final Label priorityChip = new Label();
            private final HBox root = new HBox(10, box, title, dueChip, priorityChip);

            {
                root.getStyleClass().add("task-cell");
                dueChip.getStyleClass().addAll("chip", "chip-date");
                priorityChip.getStyleClass().addAll("chip", "chip-priority");
            }

            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }
                box.setSelected(item.isCompleted());
                box.setOnAction(e -> {
                    item.setCompleted(box.isSelected());
                    pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("completed"), item.isCompleted());
                });

                title.setText(item.getTitle());
                title.getStyleClass().setAll(item.isCompleted() ? "task-title-completed" : "task-title");

                if (item.getDueDate() != null) {
                    dueChip.setText("Due " + item.getDueDate());
                    dueChip.setVisible(true);
                } else {
                    dueChip.setVisible(false);
                }

                priorityChip.setText(item.getPriority());
                priorityChip.getStyleClass().removeAll("chip-low", "chip-medium", "chip-high");
                switch (Objects.toString(item.getPriority(), "Medium")) {
                    case "Low" -> priorityChip.getStyleClass().add("chip-low");
                    case "High" -> priorityChip.getStyleClass().add("chip-high");
                    default -> priorityChip.getStyleClass().add("chip-medium");
                }

                setGraphic(root);
                pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("completed"), item.isCompleted());
            }
        };
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}