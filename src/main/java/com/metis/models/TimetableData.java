package com.metis.models;

import com.metis.controllers.TimetableController.ClassEntry;
import com.metis.dao.TimetableDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TimetableData {

    private static final TimetableData instance = new TimetableData();
    private final ObservableList<ClassEntry> classList;
    private final TimetableDAO timetableDAO = new TimetableDAO();

    private TimetableData() {
        classList = FXCollections.observableArrayList();
        // Load timetable from the database when the singleton is created
        classList.addAll(timetableDAO.getAllClasses());
    }

    public static TimetableData getInstance() {
        return instance;
    }

    public ObservableList<ClassEntry> getClassList() {
        return classList;
    }

    public void addClass(ClassEntry classEntry) {
        classList.add(classEntry);
        timetableDAO.addClass(classEntry);
    }

    public void deleteClass(ClassEntry classEntry) {
        classList.remove(classEntry);
        timetableDAO.deleteClass(classEntry.getDay(), classEntry.getSubject(), classEntry.getTime(), classEntry.getLocation());
    }
}