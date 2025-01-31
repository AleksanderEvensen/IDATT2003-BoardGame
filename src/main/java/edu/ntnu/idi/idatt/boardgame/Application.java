package edu.ntnu.idi.idatt.boardgame;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

        var btn = new Button("Hello!");
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("Hello, JavaFX!");
        });
        var box = new VBox(btn);
        box.setAlignment(javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(box, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}