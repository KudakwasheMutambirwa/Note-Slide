package com.byteclass.noteslide.controller;

import com.byteclass.noteslide.dao.DAO;
import com.byteclass.noteslide.dao.NoteDAO;
import com.byteclass.noteslide.model.Note;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddNoteController {

    @FXML private TextField textFieldTopic;
    @FXML private TextArea noteArea;
    @FXML private AnchorPane anchorPane;

    private double x, y;

    private final DAO<Note> noteDAO = NoteDAO.getInstance();

    @FXML
    void addNote(ActionEvent event){

        String topic = textFieldTopic.getText();
        String note = noteArea.getText();

        Note newNote = new Note(topic, note);

        if (topic.isEmpty() || note.isEmpty()) {
            ALERT_BOX(Alert.AlertType.ERROR, "Failed to save note", "Please fill in" +
                    " all the required fields");
            return;
        }
        // saving new note to the database
        if (noteDAO.addNote(newNote)) {
            ALERT_BOX(Alert.AlertType.INFORMATION, "Note saved successfully", "No error");
        }
    }

    @FXML
    void closeStage(MouseEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onAnchorPaneDragged(MouseEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setY(event.getScreenY() - y);
        stage.setX(event.getScreenX() - x);
    }

    @FXML
    void onAnchorPanePressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    public static void ALERT_BOX(Alert.AlertType alertType, String headerText, String contextText)
    {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.setTitle("Message");
        alert.show();
    }


}
