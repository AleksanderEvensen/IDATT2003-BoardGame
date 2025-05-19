package edu.ntnu.idi.idatt.boardgame.model;

import edu.ntnu.idi.idatt.boardgame.actions.HasTileReferenceResolver;
import java.util.Optional;
import lombok.Getter;

/**
 * A class representing a game with a board.
 * <p>
 * The game has a name, description, and an ID.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.model.Board
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
   * @param board       the game board
   * @param name        the game name
   * @param description the game description
   * @param id          the game ID
   */
  public Game(Board board, String name, String description, String id, int minPlayers,
      int maxPlayers, int numberOfDice, String imagePath) {
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
   * @see edu.ntnu.idi.idatt.boardgame.model.Board
   */
  @Override
  public void resolveReferences(Board board) {
    this.board.resolveReferences(board);
  }
}