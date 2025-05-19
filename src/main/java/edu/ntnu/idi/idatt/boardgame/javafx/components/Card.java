package edu.ntnu.idi.idatt.boardgame.javafx.components;

import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.javafx.components.enums.Size;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class Card extends BorderPane {

  /**
   * Initializes a new card with no content.
   */
  public Card() {
    super();
    getStyleClass().setAll("Card", Size.NORMAL.prefixed("rounded"));
  }

  /**
   * Initializes a new card with the given content in the center.
   *
   * @param content the component to be displayed in the center of the card
   */
  public Card(Node content) {
    this();
    setCenter(content);
  }

  public Card withRounded(Size size) {
    Utils.ensureOneOfClasses(this, size.prefixed("rounded"), Size.AllPrefixed("rounded"));
    return this;
  }
}
