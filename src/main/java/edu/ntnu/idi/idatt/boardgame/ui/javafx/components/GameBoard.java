package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.ui.TileStyleService;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.animation.AnimationQueue;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.animation.PlayerMovementAnimator;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.player.JavaFXPlayer;
import javafx.geometry.Bounds;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * A component representing the game board.
 */
public class GameBoard extends GridPane {
  private static final Logger logger = Logger.getLogger(GameBoard.class.getName());
  private final Map<Integer, TileComponent> tileComponents;
  private final Pane overlayPane;
  private final Map<Player, PlayerBlipView> playerBlips;
  private final AnimationQueue animationQueue;

  private int maxCol = 0;
  private int maxRow = 0;

  /**
   * Create a new GameBoard.
   */
  GameBoard() {
    this.tileComponents = new HashMap<>();
    this.overlayPane = new Pane();
    this.playerBlips = new ConcurrentHashMap<>();
    this.animationQueue = new AnimationQueue();


    overlayPane.setPickOnBounds(false);
    overlayPane.setManaged(false);
  }

  /**
   * Add a tile to the board.
   *
   * @param tile the tile to add
   */
  public void add(Tile tile) {
    TileComponent tileComponent = new TileComponent(tile.getTileId());
    add(tileComponent, tile.getCol(), tile.getRow());
    tileComponents.put(tile.getTileId(), tileComponent);

    if (tile.getCol() > maxCol)
      maxCol = tile.getCol();
    if (tile.getRow() > maxRow)
      maxRow = tile.getRow();
  }

  /**
   * Returns the overlay pane (for drawing ladders, lines, etc.).
   */
  public Pane getOverlayPane() {
    return overlayPane;
  }

  /**
   * Add a player to the board.
   *
   * @param player the player to add
   * @throws IllegalArgumentException if player is not a JavaFXPlayer
   */
  public void addPlayer(Player player) {
    if (!(player instanceof JavaFXPlayer)) {
      throw new IllegalArgumentException("Player must be a JavaFXPlayer");
    }

    JavaFXPlayer fxPlayer = (JavaFXPlayer) player;
    PlayerBlipView blipView = fxPlayer.createBlipView();

    playerBlips.put(player, blipView);
    overlayPane.getChildren().add(blipView);
    javafx.application.Platform.runLater(() -> {
      javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(50));
      pause.setOnFinished(e -> {
        Tile currentTile = player.getCurrentTile();
        if (currentTile != null) {
          positionPlayerBlipOnTile(player, blipView, currentTile);
        }
      });
      pause.play();
    });

    logger.fine("Added player " + player.getName() + " to the board");
  }


  /**
   * Helper method that positions a player blip on a specific tile.
   * This handles the actual positioning calculation and setting.
   *
   * @param player   the player
   * @param blipView the player's blip view
   * @param tile     the tile to position on
   */
  private void positionPlayerBlipOnTile(Player player, PlayerBlipView blipView, Tile tile) {
    TileComponent tileComponent = tileComponents.get(tile.getTileId());
    if (tileComponent == null) {
      logger.warning("No tile component found for tile ID: " + tile.getTileId());
      return;
    }

    if (tileComponent.getScene() == null ||
        tileComponent.getBoundsInLocal().getWidth() <= 0 ||
        tileComponent.getBoundsInLocal().getHeight() <= 0) {

      logger.warning("Tile component not properly laid out for tile ID: " + tile.getTileId());
      return;
    }

    Bounds tileBounds = tileComponent.localToScene(tileComponent.getBoundsInLocal());
    Bounds overlayBounds = overlayPane.sceneToLocal(tileBounds);

    double tileX = overlayBounds.getMinX() + overlayBounds.getWidth() / 2;
    double tileY = overlayBounds.getMinY() + overlayBounds.getHeight() / 2;

    blipView.setLayoutX(tileX - blipView.getPrefWidth() / 2);
    blipView.setLayoutY(tileY - blipView.getPrefHeight() / 2);

    blipView.setTranslateX(0);
    blipView.setTranslateY(0);

    logger.fine("Positioned player " + player.getName() + " on tile " + tile.getTileId() +
        " at X=" + blipView.getLayoutX() + ", Y=" + blipView.getLayoutY());
  }

  /**
   * Animates a player's movement from one tile to another.
   *
   * @param player         the player to animate
   * @param fromTile       the tile to animate from
   * @param toTile         the tile to animate to
   * @param durationMillis the duration of the animation in milliseconds
   */
  public void animatePlayerMovement(Player player, Tile fromTile, Tile toTile, int durationMillis) {
    PlayerBlipView blipView = playerBlips.get(player);
    if (blipView == null) {
      logger.warning("Attempted to animate movement of unregistered player: " + player.getName());
      return;
    }

    TileComponent toTileComponent = tileComponents.get(toTile.getTileId());
    if (toTileComponent == null) {
      logger.warning("No tile component found for destination tile ID: " + toTile.getTileId());
      return;
    }

    var animation = PlayerMovementAnimator.createPathAnimation(
        this, player, fromTile, toTile, durationMillis);

    String description = "Player " + player.getName() + " moving from tile " +
        (fromTile != null ? fromTile.getTileId() : "null") +
        " to tile " + toTile.getTileId();

    animationQueue.queue(animation, description, 500);

    logger.fine("Queued animation: " + description);
  }

  /**
   * Animates a player using a ladder from one tile to another.
   *
   * @param player         the player to animate
   * @param fromTile       the ladder start tile
   * @param toTile         the ladder end tile
   * @param durationMillis the duration of the animation in milliseconds
   */
  public void animateLadderMovement(Player player, Tile fromTile, Tile toTile, int durationMillis) {
    if (fromTile == null || toTile == null) {
      animatePlayerMovement(player, fromTile, toTile, durationMillis);
      return;
    }

    var animation = PlayerMovementAnimator.createLadderAnimation(
        this, player, fromTile, toTile, durationMillis);

    String description = "Player " + player.getName() + " ladder from tile " +
        fromTile.getTileId() + " to tile " + toTile.getTileId();

    animationQueue.queue(animation, description, 500);

    logger.fine("Queued ladder animation: " + description);
  }

  /**
   * Gets the AnimationQueue used by this GameBoard.
   *
   * @return the animation queue
   */
  public AnimationQueue getAnimationQueue() {
    return animationQueue;
  }

  /**
   * Gets a TileComponent by its tile ID.
   *
   * @param tileId the tile ID
   * @return the tile component, or null if not found
   */
  public TileComponent getTileComponent(int tileId) {
    return tileComponents.get(tileId);
  }

  /**
   * Gets a player's blip view.
   *
   * @param player the player
   * @return the player's blip view, or null if not found
   */
  public PlayerBlipView getPlayerBlipView(Player player) {
    return playerBlips.get(player);
  }

  /**
   * Span the overlay pane to cover the entire grid, and bring it to front.
   */
  private void spanOverlay() {
    getChildren().remove(overlayPane);
    add(overlayPane, 0, 0, maxCol + 1, maxRow + 1);
    overlayPane.toFront();
  }

  /**
   * A builder to create and configure a GameBoard.
   */
  public static class Builder {
    private final GameBoard gameBoard;
    private final Game game;

    public Builder(Game game) {
      this.game = game;
      gameBoard = new GameBoard();
    }

    public Builder addTiles() {
      game.getBoard().getTiles().forEach((id, tile) -> {
        gameBoard.add(tile);
      });
      return this;
    }

    /**
     * Resolves and applies styles for all tiles with actions.
     *
     * @return this builder for chaining
     */
    public Builder resolveActionStyles() {
      game.getBoard().getTiles().forEach((id, tile) -> {
        tile.getAction().ifPresent(action -> TileStyleService.applyStyle(tile, action, gameBoard));
      });
      return this;
    }

    /**
     * Adds players to the board.
     *
     * @param players the players to add
     * @return this builder for chaining
     */
    public Builder addPlayers(List<Player> players) {
      players.forEach(gameBoard::addPlayer);
      return this;
    }

    /**
     * Finalize the board: make sure the overlay spans everything.
     *
     * @return the fully built GameBoard
     */
    public GameBoard build() {
      gameBoard.spanOverlay();
      return gameBoard;
    }
  }
}
