package edu.ntnu.idi.idatt.boardgame.javafx.style;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.quiz.QuizTileAction;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.javafx.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.style.TileStyleApplier;
import java.util.List;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

/**
 * Applies styles to quiz tiles in a JavaFX application.
 * <p>
 * This class implements the TileStyleApplier interface and provides methods to apply styles to quiz
 * tiles based on their category.
 * </p>
 *
 * @version 1.0.0
 * @see TileStyleApplier
 * @since v3.0.0
 */
public class JavaFxQuizStyleApplier implements TileStyleApplier {


  /**
   * Applies styles to the given tile based on the specified action and parent.
   * <p>
   * This method sets the icon and background color of the tile based on its category.
   * </p>
   *
   * @param tile   the tile to apply styles to
   * @param action the action associated with the tile
   * @param parent the parent node containing the tile
   */
  @Override
  public void applyStyle(Tile tile, TileAction action, Object parent) {
    if (!(parent instanceof Pane pane) || !(action instanceof QuizTileAction)) {
      return;
    }

    List<TileComponent> tileComponents =
        pane.getChildren().filtered(node -> node instanceof TileComponent).stream()
            .map(node -> (TileComponent) node).toList();

    TileComponent actionTile = tileComponents.stream()
        .filter(tileComponent -> tileComponent.getTileId() == tile.getTileId()).findFirst()
        .orElse(null);

    if (actionTile == null) {
      return;
    }

    Tooltip.install(actionTile, new Tooltip("This tile gives a quiz question in the category: "
        + ((QuizTileAction) action).getCategory().name()));

    actionTile.setIcon(mapCategoryToIcon((QuizTileAction) action), 0, Color.WHITE);
    actionTile.setBackgroundColor(Color.valueOf(mapCategoryToColor((QuizTileAction) action)));
  }

  /**
   * Maps the quiz category to an icon.
   *
   * @param action the quiz tile action
   * @return the icon
   */
  private Ikon mapCategoryToIcon(QuizTileAction action) {
    return switch (action.getCategory()) {
      case GENERAL_KNOWLEDGE -> FontAwesomeSolid.BOOK;
      case SCIENCE -> FontAwesomeSolid.FLASK;
      case HISTORY -> FontAwesomeSolid.HISTORY;
      case GEOGRAPHY -> FontAwesomeSolid.GLOBE;
      case ENTERTAINMENT -> FontAwesomeSolid.TV;
      case ART -> FontAwesomeSolid.PAINT_BRUSH;
      case SPORTS -> FontAwesomeSolid.FOOTBALL_BALL;
      case RANDOM -> FontAwesomeSolid.QUESTION_CIRCLE;
    };
  }


  /**
   * Maps the quiz category to a color.
   *
   * @param action the quiz tile action
   * @return the color as a string
   */
  private String mapCategoryToColor(QuizTileAction action) {
    return switch (action.getCategory()) {
      case GENERAL_KNOWLEDGE -> "#FF5733";
      case SCIENCE -> "#33FF57";
      case HISTORY -> "#3357FF";
      case GEOGRAPHY -> "#FF33A1";
      case ENTERTAINMENT -> "#A133FF";
      case ART -> "#33FFF5";
      case RANDOM -> "#33FFF2";
      case SPORTS -> "#FF33F2";
    };
  }

}
