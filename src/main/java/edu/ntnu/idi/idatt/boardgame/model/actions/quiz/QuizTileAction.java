package edu.ntnu.idi.idatt.boardgame.model.actions.quiz;

import edu.ntnu.idi.idatt.boardgame.model.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.entities.QuestionCategory;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;
import java.util.logging.Logger;
import lombok.Getter;


/**
 * Represents an action where a player gets a quiz question.
 * <p>
 * This action is triggered when a player lands on a tile with a quiz.
 * </p>
 *
 * @version v1.0.0
 * @see Tile
 * @see Player
 * @since v3.0.0
 */
public class QuizTileAction implements TileAction {

  private static final Logger logger = Logger.getLogger(QuizTileAction.class.getName());
  @Getter
  private QuestionCategory category;

  /**
   * Performs the action on the specified player.
   *
   * @param player the player on whom the action is performed
   * @return true if the action was performed successfully, false otherwise
   * @throws IllegalArgumentException if the player is null
   * @see Player
   */
  @Override
  public boolean perform(Player player) {
    return true;
  }
}
