package com.byteclass.noteslide.controller;

import com.byteclass.noteslide.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

import static com.byteclass.noteslide.controller.AddNoteController.ALERT_BOX;

public class SettingsController implements Initializable
{

    @FXML private AnchorPane anchorPane;
    @FXML private RadioButton rbMinutes;
    @FXML private RadioButton rbSeconds;
    @FXML private TextField timeField;
    @FXML private CheckBox checkBox;

    public static int INTERVAL_TIME = 2000;
    public static boolean IS_MINUTE;

    double x, y;

    private String username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Properties system = System.getProperties();
        username = system.getProperty("user.name");

        rbSeconds.setSelected(true);
        checkBox.setSelected(checkState().equals("true"));
    }

    @FXML
    void applyNewIntervalTime(ActionEvent event)
    {
        try
        {
            INTERVAL_TIME = Integer.parseInt(timeField.getText().trim());
            timeSelection();

            saveTime(timeField.getText().trim(), (IS_MINUTE ? "T" : "F"));

            ALERT_BOX(Alert.AlertType.INFORMATION, "Time applied", "");

        }catch (NumberFormatException e){

            ALERT_BOX(Alert.AlertType.INFORMATION, "Number format exception",
                    "Type only numbers");
        }

    }

    @FXML
    private void timeSelection()
    {
        if (rbMinutes.isSelected()){
            IS_MINUTE = true;
            System.out.println("True");
        }else{
            IS_MINUTE = false;
            System.out.println("False");
        }
    }

    @FXML
    void back(ActionEvent event) throws IOException
    {
        Parent parent = FXMLLoader.load(Objects.requireNonNull
                (MainApplication.class.getResource("main-view.fxml")));
        Scene scene = new Scene(parent);
       // scene.getStylesheets().add(MainApplication.class.getResource("main.css").toExternalForm());
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void runOnSystemStartUp(ActionEvent event)
    {
        String path = "C:\\Users\\" + username +
                "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\noteslide.bat";

        

        try{

            FileOutputStream outputStream = new FileOutputStream(path);
            String batchCode = "start C:\\\"Program Files (x86)\\NoteSlide\\noteslide.exe\"";

            if (checkBox.isSelected()){
                saveState(username, "true");
                outputStream.write(batchCode.getBytes());
            }else {
                saveState(username, "false");
                deleteFile(path);
            }
            outputStream.flush();
            outputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void onAnchorPaneDragged(MouseEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void onAnchorPanePressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    private void saveState(String username, String state) throws IOException
    {

        String path = "C:\\Users\\" + username +
                "\\AppData\\Local\\NoteSlide\\note_slide.dat";

        FileOutputStream outputStream = new FileOutputStream(path);
        outputStream.write(state.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private String checkState(){

        String path = "C:\\Users\\" + username +
                "\\AppData\\Local\\NoteSlide\\note_slide.dat";
        StringBuilder result = new StringBuilder();

        try{

            FileInputStream inputStream = new FileInputStream(path);

            int line;
            while ( (line = inputStream.read()) != -1){
               result.append((char) line);
            }
            inputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
        return String.format("%s", result);
    }

    private void deleteFile(String path){
        Thread thread = new Thread(() ->
        {
            Path batFilePath = FileSystems.getDefault().getPath(path);
            try
            {
                Files.deleteIfExists(batFilePath);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            System.out.println("File deleted");
        });
        thread.start();
    }

    private void saveTime(String time, String typeOfTime){

        FileOutputStream outputStream;
        String path = "C:\\Users\\" + username +
                "\\AppData\\Local\\NoteSlide\\time.txt";
        try{
            outputStream = new FileOutputStream(path);
            String line = typeOfTime + " " + time;
            outputStream.write(line.getBytes());

            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
