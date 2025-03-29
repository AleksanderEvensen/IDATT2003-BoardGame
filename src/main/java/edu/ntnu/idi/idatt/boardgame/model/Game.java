package edu.ntnu.idi.idatt.boardgame.model;

import edu.ntnu.idi.idatt.boardgame.actions.HasTileReferenceResolver;

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

    private final Board board;
    private final String name;
    private final String description;
    private final String id;
    private final int minPlayers;
    private final int maxPlayers;
    private final int numberOfDice;

    /**
     * Constructs a game with the specified board, name, description, and ID.
     *
     * @param board the game board
     * @param name the game name
     * @param description the game description
     * @param id the game ID
     */
    public Game(Board board, String name, String description, String id, int minPlayers, int maxPlayers, int numberOfDice) {
        this.board = board;
        this.name = name;
        this.description = description;
        this.id = id;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.numberOfDice = numberOfDice;

    }

    /**
     * Returns the game ID.
     *
     * @return the game ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the game board.
     *
     * @return the game board
     * @see edu.ntnu.idi.idatt.boardgame.model.Board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the game name.
     *
     * @return the game name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the game description.
     *
     * @return the game description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the minimum number of players required to play the game.
     *
     * @return the minimum number of players
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Returns the maximum number of players that can play the game.
     *
     * @return the maximum number of players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Returns the number of dice used in the game.
     *
     * @return the number of dice
     */
    public int getNumberOfDice() {
        return numberOfDice;
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