package com.metis.models;

import com.metis.dao.TaskDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskData {

    private static final TaskData instance = new TaskData();
    private final ObservableList<Task> tasks;
    private final TaskDAO taskDAO = new TaskDAO();

    private TaskData() {
        tasks = FXCollections.observableArrayList();
        // Load tasks from the database when the singleton is created
        tasks.addAll(taskDAO.getAllTasks());
    }

    public static TaskData getInstance() {
        return instance;
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
        taskDAO.addTask(task);
    }

    public void updateTask(Task task) {
        taskDAO.updateTask(task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
        taskDAO.deleteTask(task.getTitle());
    }
}