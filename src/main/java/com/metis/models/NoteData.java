package com.metis.models;

import com.metis.dao.NoteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.stream.Collectors;

public class NoteData {

    private static final NoteData instance = new NoteData();
    private final ObservableList<Note> notes;
    private final NoteDAO noteDAO = new NoteDAO();

    private NoteData() {
        notes = FXCollections.observableArrayList();
        // Load notes from the database when the singleton is created
        notes.addAll(noteDAO.getAllNotes());
    }

    public static NoteData getInstance() {
        return instance;
    }

    public ObservableList<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        notes.add(note);
        noteDAO.addNote(note);
    }

    public void updateNote(Note note) {
        // Find the existing note and update it
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getTitle().equals(note.getTitle())) {
                notes.set(i, note);
                break;
            }
        }
        noteDAO.updateNote(note);
    }

    public void deleteNote(String title) {
        notes.removeIf(note -> note.getTitle().equals(title));
        noteDAO.deleteNote(title);
    }
}