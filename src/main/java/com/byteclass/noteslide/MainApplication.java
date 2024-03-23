package com.byteclass.noteslide;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApplication extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 446, 240);

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
       // scene.getStylesheets().add(MainApplication.class.getResource("main.css").toExternalForm());
        stage.setX(screen.getMinX() + screen.getWidth() - 446);
        stage.setY(0);
        stage.setScene(scene);
        stage.setTitle("NoteSlide");
        stage.setOpacity(0.9);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        Image image = new Image("note_slide.png");
        stage.getIcons().add(image);

        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
        
        stage.show();

        stage.setOnCloseRequest(windowEvent -> {
            System.exit(0);
        });
    }
}