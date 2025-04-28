package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

public class Button extends javafx.scene.control.Button {


    public Button(String text) {
        super(text);
        this.getStyleClass().add("custom-button");
    }

    public Button() {
        this("");
    }
}
