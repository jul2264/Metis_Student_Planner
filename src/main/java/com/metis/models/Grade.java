package com.metis.models;

import javafx.beans.property.*;

public class Grade {
    private final StringProperty course;
    private final IntegerProperty credits;
    private final StringProperty grade;
    private final DoubleProperty points;

    public Grade(String course, int credits, String grade, double points) {
        this.course = new SimpleStringProperty(course);
        this.credits = new SimpleIntegerProperty(credits);
        this.grade = new SimpleStringProperty(grade);
        this.points = new SimpleDoubleProperty(points);
    }

    public StringProperty courseProperty() { return course; }
    public IntegerProperty creditsProperty() { return credits; }
    public StringProperty gradeProperty() { return grade; }
    public DoubleProperty pointsProperty() { return points; }

    public String getCourse() { return course.get(); }
    public int getCredits() { return credits.get(); }
    public String getGrade() { return grade.get(); }
    public double getPoints() { return points.get(); }
}
