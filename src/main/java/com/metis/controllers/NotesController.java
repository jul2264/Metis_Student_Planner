package com.metis.controllers;

import com.metis.models.Note;
import com.metis.models.NoteData;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class NotesController {

    @FXML private TextField noteTitleField;
    @FXML private TextArea noteContentArea;
    @FXML private ListView<String> notesList;
    @FXML private Canvas noteCanvas;
    @FXML private Button clearCanvasButton;
    @FXML private Button saveButton;

    private final NoteData noteData = NoteData.getInstance();
    private boolean isDrawMode = false;
    private GraphicsContext gc;
    private static final String DRAWING_PREFIX = "[DRAWING]";
    private static final String DRAWINGS_DIR = "notes_drawings";

    @FXML
    private void initialize() {
        System.out.println("✅ NotesController initialized");

        gc = noteCanvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE); // Pen color is white
        gc.setLineWidth(2);

        // Set the default background to black
        setCanvasBackground();

        noteCanvas.setOnMousePressed(e -> gc.beginPath());
        noteCanvas.setOnMouseDragged(e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        updateNotesList();
        noteData.getNotes().addListener((ListChangeListener<Note>) c -> updateNotesList());
        
        new File(DRAWINGS_DIR).mkdirs();
        switchToTextMode();
    }

    @FXML
    private void saveNote() {
        String title = noteTitleField.getText().trim();
        if (title.isEmpty()) {
            showAlert("Missing Title", "Please enter a title before saving.");
            return;
        }

        String content;
        if (isDrawMode) {
            String imagePath = saveCanvasToFile(title);
            if (imagePath == null) {
                showAlert("Save Error", "Could not save the drawing.");
                return;
            }
            content = DRAWING_PREFIX + imagePath;
        } else {
            content = noteContentArea.getText();
        }

        Note newNote = new Note(title, content);
        boolean noteExists = noteData.getNotes().stream().anyMatch(n -> n.getTitle().equals(title));

        if (noteExists) {
            noteData.updateNote(newNote);
        } else {
            noteData.addNote(newNote);
        }

        clearInputs();
        showAlert("Saved", "Note saved successfully!");
    }

    @FXML
    private void loadSelectedNote() {
        String selectedTitle = notesList.getSelectionModel().getSelectedItem();
        if (selectedTitle == null) return;

        noteData.getNotes().stream()
            .filter(note -> note.getTitle().equals(selectedTitle))
            .findFirst()
            .ifPresent(note -> {
                noteTitleField.setText(note.getTitle());
                String content = note.getContent();

                if (content != null && content.startsWith(DRAWING_PREFIX)) {
                    switchToDrawMode();
                    clearCanvas(); // Clear before drawing
                    String imagePath = content.substring(DRAWING_PREFIX.length());
                    File imageFile = new File(imagePath);

                    if (imageFile.exists()) {
                        try {
                            Image image = new Image(imageFile.toURI().toString());
                            gc.drawImage(image, 0, 0, noteCanvas.getWidth(), noteCanvas.getHeight());
                        } catch (Exception e) {
                            System.err.println("Error loading drawing image: " + e.getMessage());
                            showAlert("Load Error", "Could not display the saved drawing.");
                        }
                    } else {
                        System.err.println("Drawing file not found: " + imagePath);
                        showAlert("Load Error", "The drawing file could not be found.");
                    }
                } else {
                    switchToTextMode();
                    noteContentArea.setText(content);
                }
            });
    }

    private String saveCanvasToFile(String title) {
        try {
            // Create snapshot parameters to set a black background for the saved image
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.BLACK);

            WritableImage writableImage = new WritableImage((int) noteCanvas.getWidth(), (int) noteCanvas.getHeight());
            noteCanvas.snapshot(params, writableImage); // Use the parameters

            String safeTitle = title.replaceAll("[^a-zA-Z0-9.-]", "_");
            File file = new File(DRAWINGS_DIR + File.separator + safeTitle + ".png");
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void switchToTextMode() {
        isDrawMode = false;
        noteContentArea.setVisible(true);
        noteCanvas.setVisible(false);
        clearCanvasButton.setVisible(false);
        saveButton.setText("Save Text");
    }

    @FXML
    private void switchToDrawMode() {
        isDrawMode = true;
        noteContentArea.setVisible(false);
        noteCanvas.setVisible(true);
        clearCanvasButton.setVisible(true);
        saveButton.setText("Save Drawing");
    }

    @FXML
    private void clearCanvas() {
        // Now clears to black instead of transparent
        setCanvasBackground();
    }
    
    private void setCanvasBackground() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, noteCanvas.getWidth(), noteCanvas.getHeight());
    }

    private void updateNotesList() {
        notesList.getItems().clear();
        for (Note note : noteData.getNotes()) {
            notesList.getItems().add(note.getTitle());
        }
    }

    private void clearInputs() {
        noteTitleField.clear();
        noteContentArea.clear();
        clearCanvas();
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}