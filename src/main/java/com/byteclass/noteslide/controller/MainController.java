package com.byteclass.noteslide.controller;

import atlantafx.base.theme.Styles;
import com.byteclass.noteslide.MainApplication;
import com.byteclass.noteslide.dao.DAO;
import com.byteclass.noteslide.dao.NoteDAO;
import com.byteclass.noteslide.model.Note;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainController implements Initializable
{

    @FXML private Label labelTopic;
    @FXML private Label labelNote;
    @FXML private ImageView settingsImageView;
    @FXML private AnchorPane anchorPane;
    @FXML private Label labelAddNote;

    private double x, y;

    private final DAO<Note> noteDAO = NoteDAO.getInstance();
    private String username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Properties properties = System.getProperties();
        username = properties.getProperty("user.name");

        labelAddNote.setText(null);
        labelAddNote.setGraphic(new FontIcon(Feather.PLUS_CIRCLE));

        anchorPane.getStyleClass().add(Styles.ELEVATED_3);

        Thread thread = new Thread(backGroundProcess());
        thread.start();

        createNoteSlideFolder();
        openAddNoteView();
    }

    public Runnable backGroundProcess()
    {
        return () ->
        {
            ObservableList<Note> notes = noteDAO.getAllNotes();
            do{

                Collections.shuffle(notes);
                for (Note note: notes){

                    Platform.runLater(()-> {
                        labelTopic.setText(note.getTopic());
                        labelNote.setText(note.getNote());
                    });
                    try {
                        Thread.sleep(getSavedTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }while(notes.size() > 1);
        };
    }


    void openAddNoteView()
    {
        labelAddNote.setOnMouseClicked(event -> {
            Parent parent = null;
            try {
                parent = FXMLLoader.load(MainApplication.class.getResource("add-note-view.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Scene scene = new Scene(parent);
            // scene.getStylesheets().add(MainApplication.class.getResource("main.css").toExternalForm());
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.initOwner(anchorPane.getScene().getWindow());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setOpacity(0.9);

            stage.show();
        });
    }

    @FXML
    void openSettingsForm(MouseEvent event) throws IOException
    {
        Parent parent = FXMLLoader.load(MainApplication.class.getResource("settings-view.fxml"));
        Scene scene = new Scene(parent);
        //scene.getStylesheets().add(MainApplication.class.getResource("main.css").toExternalForm());
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onAnchorPaneDragged(MouseEvent event)
    {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void onAnchorPanePressed(MouseEvent event)
    {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void closeApplication(MouseEvent event)
    {
        System.exit(0);
    }
    private int getSavedTime(){

        FileInputStream inputStream;
        StringBuilder result = new StringBuilder();
        int finalResult = 0;

        File file = new File(getPath());
        if (file.exists()){

            try{

                inputStream = new FileInputStream(getPath());

                int line;
                while ((line = inputStream.read()) != -1){
                    result.append((char)line);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            String retrievedTime = result.toString();
            int time = Integer.parseInt(retrievedTime.substring(2));

            if (retrievedTime.startsWith("T")){
                finalResult = time * 60000;
            }else {
                finalResult = time * 1000;
            }
        }else{
            finalResult = 5000;
        }

        return finalResult;
    }

    private String getPath(){

        String path = "C:\\Users\\" + username +
                "\\AppData\\Local\\NoteSlide\\time.txt";

        return path;
    }

    private void createNoteSlideFolder(){

        String path = "C:\\Users\\" + username +
                "\\AppData\\Local\\NoteSlide\\";

        File file = new File(path);

        if (!file.exists()){
            boolean result = file.mkdir();
            if (result){
                System.out.println("Folder created");
            }else{
                System.out.println("Folder not created");
            }
        }

    }
}
