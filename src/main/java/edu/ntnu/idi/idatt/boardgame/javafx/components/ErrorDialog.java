package edu.ntnu.idi.idatt.boardgame.javafx.components;

import edu.ntnu.idi.idatt.boardgame.javafx.components.Header.HeaderType;
import edu.ntnu.idi.idatt.boardgame.javafx.components.enums.Weight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * A dialog component for displaying error messages to the user.
 *
 * <p>
 * This dialog appears as an overlay with a semi-transparent background and a card containing the
 * error message and an OK button to dismiss it.
 * </p>
 *
 * @since v4.0.0
 */
public class ErrorDialog {

  private static final double PADDING = 24;

  private final StackPane owner;
  private final Runnable onClose;

  /**
   * Creates an error dialog to display
   *
   * @param owner   the owner root {@link StackPane}
   * @param onClose a method that runns when the dialog is closed
   */
  public ErrorDialog(StackPane owner, Runnable onClose) {
    this.owner = owner;
    this.onClose = onClose;
  }

  /**
   * Shows the error dialog as an overlay on the owner StackPane.
   */
  public void show(String title, String description) {
    StackPane overlay = new StackPane();
    overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
    overlay.setPrefSize(owner.getWidth(), owner.getHeight());

    Card dialogCard = new Card();
    dialogCard.setPadding(new Insets(PADDING));
    dialogCard.setMaxWidth(Region.USE_PREF_SIZE);
    dialogCard.setMaxHeight(Region.USE_PREF_SIZE);
    dialogCard.setPrefWidth(500);

    Header header = new Header("Error: " + title);
    header.withType(HeaderType.H2).withFontWeight(Weight.SEMIBOLD);
    header.setWrapText(true);

    Header messageLabel = new Header(description).withType(HeaderType.H5);
    messageLabel.setWrapText(true);
    messageLabel.setMaxWidth(350);

    Button okButton = new Button("OK");
    okButton.setOnAction(evt -> {
      owner.getChildren().remove(overlay);
      this.onClose.run();
    });
    var footer = new VBox(10, okButton);
    footer.setAlignment(Pos.BOTTOM_RIGHT);
    footer.setPadding(new Insets(10));

    VBox content = new VBox(20, header, messageLabel);
    content.setAlignment(Pos.TOP_LEFT);
    dialogCard.setCenter(content);
    dialogCard.setBottom(footer);

    StackPane.setAlignment(dialogCard, Pos.CENTER);
    overlay.getChildren().add(dialogCard);

    owner.getChildren().add(overlay);
  }
}
