package edu.ntnu.idi.idatt.boardgame.model.entities;

import edu.ntnu.idi.idatt.boardgame.model.actions.HasTileReferenceResolver;
import java.util.Optional;
import lombok.Getter;

/**
 * A class representing a game with a board.
 * <p>
 * The game has a name, description, and an ID.
 * </p>
 *
 * @see Board
 * @since v1.0.0
 */
public class Game implements HasTileReferenceResolver {

  @Getter
  private final Board board;
  @Getter
  private final String name;
  @Getter
  private final String description;
  @Getter
  private final String id;
  @Getter
  private final int minPlayers;
  @Getter
  private final int maxPlayers;
  @Getter
  private final int numberOfDice;
  private final String imagePath;

  /**
   * Constructs a game with the specified board, name, description, and ID.
   *
   * @param board        the game board
   * @param name         the game name
   * @param description  the game description
   * @param id           the game ID
   * @param minPlayers   the minimum number of players
   * @param maxPlayers   the maximum number of players
   * @param numberOfDice the number of dice used in the game
   * @param imagePath    the path to the game's image
   * @throws IllegalArgumentException if any numeric value is invalid
   */
  public Game(Board board, String name, String description, String id, int minPlayers,
      int maxPlayers, int numberOfDice, String imagePath) {
    if (minPlayers <= 0) {
      throw new IllegalArgumentException("Minimum players must be greater than 0.");
    }
    if (maxPlayers < minPlayers || maxPlayers > 10) {
      throw new IllegalArgumentException(
          "Maximum players must be greater than or equal to minimum players and less than or equal to 10.");
    }
    if (numberOfDice < 0 || numberOfDice > 10) {
      throw new IllegalArgumentException("Number of dice cannot be negative or greater than 10.");
    }

    this.board = board;
    this.name = name;
    this.description = description;
    this.id = id;
    this.minPlayers = minPlayers;
    this.maxPlayers = maxPlayers;
    this.numberOfDice = numberOfDice;
    this.imagePath = imagePath;
  }

  /**
   * Returns the path to the image of the game.
   *
   * @return the path to the image
   */
  public Optional<String> getImagePath() {
    return Optional.ofNullable(imagePath);
  }

  /**
   * Resolves references to other tiles on the board.
   *
   * @param board the board containing the tiles
   * @see Board
   */
  @Override
  public void resolveReferences(Board board) {
    this.board.resolveReferences(board);
  }
}