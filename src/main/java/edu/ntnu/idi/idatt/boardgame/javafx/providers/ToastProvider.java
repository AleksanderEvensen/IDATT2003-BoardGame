package edu.ntnu.idi.idatt.boardgame.javafx.providers;

import edu.ntnu.idi.idatt.boardgame.javafx.components.Toast;
import edu.ntnu.idi.idatt.boardgame.javafx.components.enums.ToastStyle;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * A singleton class that provides a toast notification system.
 * <p>
 * This class is used to create and display toast notifications in the application. It manages the
 * lifecycle of the toast notifications, including their appearance and disappearance.
 * </p>
 *
 * @since v3.0.0
 * @version 1.0.0
 */
public final class ToastProvider {
  private static StackPane rootPane;
  private static final VBox toastBox = new VBox(8);

  private ToastProvider() {}

  /**
   * Initializes the ToastProvider with the given root pane.
   *
   * @param root the root pane to which the toast notifications will be added
   */
  public static void setRoot(StackPane root) {
    rootPane = root;
    toastBox.setPickOnBounds(false);
    toastBox.setMouseTransparent(true);
    toastBox.setPadding(new javafx.geometry.Insets(20));
    StackPane.setAlignment(toastBox, javafx.geometry.Pos.TOP_RIGHT);

    rootPane.getChildren().add(toastBox);
  }

  /**
   * Displays a toast notification with the given message and a default lifetime of 3 seconds.
   *
   * @param message the message to display in the toast notification
   */
  public static void show(String message) { show(message, Duration.seconds(3), ToastStyle.INFO); }

  /**
   * Displays a toast notification with the given message and lifetime.
   *
   * @param message  the message to display in the toast notification
   * @param lifeTime the duration for which the toast notification will be displayed
   */
  public static void show(String message, Duration lifeTime, ToastStyle style) {
    if (rootPane == null) {
      throw new IllegalStateException("Call ToastManager.setRoot(root) first.");
    }
    Toast toast = new Toast(message, style);
    toastBox.setAlignment(Pos.BOTTOM_RIGHT);
    toastBox.getChildren().addLast(toast);
    toast.buildLifecycle(lifeTime).play();
  }
}

