package com.metis.models;

import com.metis.controllers.GpaController.Course;
import com.metis.dao.GpaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GpaData {

    private static final GpaData instance = new GpaData();
    private final ObservableList<Course> courses;
    private final GpaDAO gpaDAO = new GpaDAO();

    private GpaData() {
        courses = FXCollections.observableArrayList();
        // Load courses from the database
        courses.addAll(gpaDAO.getAllCourses());
    }

    public static GpaData getInstance() {
        return instance;
    }

    public ObservableList<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
        gpaDAO.addCourse(course);
    }

    public void deleteCourse(Course course) {
        courses.remove(course);
        gpaDAO.deleteCourse(course.getSubject());
    }
}