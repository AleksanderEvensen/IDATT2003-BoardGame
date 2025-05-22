package edu.ntnu.idi.idatt.boardgame.javafx.components;

import edu.ntnu.idi.idatt.boardgame.javafx.components.enums.ToastStyle;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;

/**
 * A toast notification that appears at the bottom of the screen.
 * <p>
 * This class is used to create a toast notification that can be displayed at the bottom of the
 * screen. The toast notification will fade in, stay for a specified duration, and then fade out.
 * </p>
 *
 * @since v3.0.0
 * @version 1.0.0
 */
public class Toast extends VBox {

  /**
   * Constructs a toast notification with the specified text.
   *
   * @param text the text to display in the toast notification
   */
  public Toast(String text, ToastStyle style) {
    Label message = new Label(text);
    message.getStyleClass().add("toast-message");
    message.setWrapText(true);
    message.setMaxWidth(Double.MAX_VALUE);
    message.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(message, Priority.ALWAYS);

    setAlignment(Pos.CENTER_LEFT);
    setPadding(new Insets(10, 16, 10, 16));
    setSpacing(8);
    setMaxWidth(300);
    setMaxHeight(500);
    getStyleClass().add("toast");
    getStyleClass().add(style.toString());
    getChildren().add(message);
    setOpacity(0);
    setTranslateY(-50);
  }

  public Animation buildLifecycle(Duration lifeTime) {
    KeyFrame kfIn1 = new KeyFrame(Duration.millis(0),
        new KeyValue(opacityProperty(), 0),
        new KeyValue(translateYProperty(), -50));
    KeyFrame kfIn2 = new KeyFrame(Duration.millis(250),
        new KeyValue(opacityProperty(), 1),
        new KeyValue(translateYProperty(), 0));

    KeyFrame kfOut1 = new KeyFrame(lifeTime.subtract(Duration.millis(250)),
        new KeyValue(opacityProperty(), 1),
        new KeyValue(translateYProperty(), 0));
    KeyFrame kfOut2 = new KeyFrame(lifeTime,
        new KeyValue(opacityProperty(), 0),
        new KeyValue(translateYProperty(), -50));

    Timeline t = new Timeline(kfIn1, kfIn2, kfOut1, kfOut2);
    t.setOnFinished(e -> ((Pane) getParent()).getChildren().remove(this));
    return t;
  }
}