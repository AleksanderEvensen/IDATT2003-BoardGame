package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

import edu.ntnu.idi.idatt.boardgame.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.game.GameController;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.ui.TileStyleService;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.animation.AnimationQueue;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.animation.PlayerMovementAnimator;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.style.LadderUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import lombok.Getter;

/**
 * A component representing the game board.
 */
public class GameBoard extends GridPane {

  private static final Logger logger = Logger.getLogger(GameBoard.class.getName());
  private final Map<Integer, TileComponent> tileComponents;

  @Getter
  private final Pane overlayPane;

  @Getter
  private final AnimationQueue animationQueue;

  private final Map<Player, PlayerBlipView> playerBlips;
  private final GameController gameController;
  private final Map<TileComponent, LadderComponent> ladderComponents;

  private int maxCol = 0;
  private int maxRow = 0;

  /**
   * Creates a new GameBoard.
   *
   * @param gameController The game controller managing the game logic.
   * @param animationQueue The animation queue for handling animations.
   */
  public GameBoard(GameController gameController, AnimationQueue animationQueue) {
    this.tileComponents = new HashMap<>();
    this.overlayPane = new Pane();
    this.playerBlips = new ConcurrentHashMap<>();
    this.animationQueue = animationQueue;
    this.gameController = gameController;
    this.ladderComponents = new ConcurrentHashMap<>();

    setupResizingListener();
  }

  /**
   * Adds a tile to the board.
   *
   * @param tile The tile to add.
   */
  public void add(Tile tile) {
    TileComponent tileComponent = new TileComponent(tile.getTileId());
    add(tileComponent, tile.getCol(), tile.getRow());
    tileComponents.put(tile.getTileId(), tileComponent);

      if (tile.getCol() > maxCol) {
          maxCol = tile.getCol();
      }
      if (tile.getRow() > maxRow) {
          maxRow = tile.getRow();
      }
  }

  /**
   * Adds a player to the board.
   *
   * @param player The player to add.
   * @throws IllegalArgumentException If the player is not a JavaFXPlayer.
   */
  public void addPlayer(Player player) {
    PlayerBlipView blipView = new PlayerBlipView(player);
    playerBlips.put(player, blipView);
    overlayPane.getChildren().add(blipView);
    Platform.runLater(() -> {
      javafx.animation.PauseTransition pause =
          new javafx.animation.PauseTransition(javafx.util.Duration.millis(50));
      pause.setOnFinished(e -> {
        Tile currentTile = player.getCurrentTile();
        if (currentTile != null) {
          positionPlayerBlipOnTile(player, blipView, currentTile);
        }
        blipView.setViewOrder(-1);
      });
      pause.play();
    });

    logger.fine("Added player " + player.getName() + " to the board");
  }

  /**
   * Positions a player blip on a specific tile.
   *
   * @param player   The player to position.
   * @param blipView The player's blip view.
   * @param tile     The tile to position the player on.
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
   * @param player   The player to animate.
   * @param fromTile The tile to animate from.
   * @param toTile   The tile to animate to.
   */
  public void animatePlayerMovement(Player player, Tile fromTile, Tile toTile) {
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
        this, player, fromTile, toTile);

    String description = "Player " + player.getName() + " moving from tile " +
        (fromTile != null ? fromTile.getTileId() : "null") +
        " to tile " + toTile.getTileId();

    animationQueue.queue(animation, description, 500);
    logger.fine("Queued animation: " + description);
  }

  /**
   * Animates a player using a ladder from one tile to another.
   *
   * @param player   The player to animate.
   * @param fromTile The ladder start tile.
   * @param toTile   The ladder end tile.
   */
  public void animateLadderMovement(Player player, Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null) {
      animatePlayerMovement(player, fromTile, toTile);
      return;
    }

    var animation = PlayerMovementAnimator.createLadderAnimation(
        this, player, fromTile, toTile);

    String description = "Player " + player.getName() + " ladder from tile " +
        fromTile.getTileId() + " to tile " + toTile.getTileId();

    animationQueue.queue(animation, description, 500);

    logger.fine("Queued ladder animation: " + description);
  }

  /**
   * Gets a TileComponent by its tile ID.
   *
   * @param tileId The tile ID.
   * @return The TileComponent, or null if not found.
   */
  public TileComponent getTileComponent(int tileId) {
    return tileComponents.get(tileId);
  }

  /**
   * Gets a player's blip view.
   *
   * @param player The player.
   * @return The player's blip view, or null if not found.
   */
  public PlayerBlipView getPlayerBlipView(Player player) {
    return playerBlips.get(player);
  }

  /**
   * Adds a ladder component to the board.
   *
   * @param tileComponent   The tile component associated with the ladder.
   * @param ladderComponent The ladder component to add.
   */
  public void addLadderComponent(TileComponent tileComponent, LadderComponent ladderComponent) {
    if (ladderComponents.containsKey(tileComponent)) {
      logger.warning("Ladder component already exists for tile: " + tileComponent.getTileId());
      return;
    }
    ladderComponents.put(tileComponent, ladderComponent);
    overlayPane.getChildren().add(ladderComponent);
  }

  /**
   * Sets up listeners to fix the position of ladder components and player blips when the window is
   * resized.
   */
  private void setupResizingListener() {
    this.boundsInParentProperty().addListener((obs, oldBounds, newBounds) -> {
      redrawLadderComponents();
      recenterPlayerBlips();
    });

    tileComponents.values().forEach(tileComponent -> {
      tileComponent.boundsInParentProperty().addListener((obs, oldBounds, newBounds) -> {
        redrawLadderComponents();
        recenterPlayerBlips();
      });
    });

    this.sceneProperty().addListener((obs, oldScene, newScene) -> {
      if (newScene != null) {
        newScene.widthProperty().addListener((o, oldWidth, newWidth) -> redrawLadderComponents());
        newScene.heightProperty()
            .addListener((o, oldHeight, newHeight) -> redrawLadderComponents());
      }
    });
  }

  /**
   * Redraws all ladder components on the board.
   */
  private void redrawLadderComponents() {

    /// applyCss() and layout() is suggested by ChatGPT to ensure the layout is updated before calculating coordinates
    this.applyCss();
    this.layout();

    ladderComponents.forEach((tileComponent, ladderComponent) -> {
      Tile tile = gameController.getGame().getBoard().getTile(tileComponent.getTileId());
      if (tile != null && tile.getAction().isPresent() && tile.getAction()
          .get() instanceof LadderAction ladderAction) {

        TileComponent destinationTileComponent =
            tileComponents.get(ladderAction.getDestinationTile().getTileId());

        List<Double> startCoords = LadderUtils.calculateTileCenter(this, tileComponent);
        List<Double> endCoords = LadderUtils.calculateTileCenter(this, destinationTileComponent);

        ladderComponent.updateCoordinates(
            startCoords.get(0),
            startCoords.get(1),
            endCoords.get(0),
            endCoords.get(1)
        );
      }
    });
  }

  /**
   * Recenters all player blips on their respective tiles when the window is resized.
   */
  public void recenterPlayerBlips() {
    playerBlips.forEach((player, blipView) -> {
      Tile currentTile = player.getCurrentTile();
      if (currentTile != null) {
        positionPlayerBlipOnTile(player, blipView, currentTile);
      }
    });
  }

  /**
   * Spans the overlay pane to cover the entire grid and brings it to the front.
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

    /**
     * Creates a new Builder for the GameBoard.
     *
     * @param gameController The game controller managing the game logic.
     * @param animationQueue The animation queue for handling animations.
     */
    public Builder(GameController gameController, AnimationQueue animationQueue) {
      this.game = gameController.getGame();
      gameBoard = new GameBoard(gameController, animationQueue);
    }

    /**
     * Adds all tiles from the game board to the GameBoard.
     *
     * @return This builder for chaining.
     */
    public Builder addTiles() {
      game.getBoard().getTiles().forEach((id, tile) -> {
        gameBoard.add(tile);
      });
      return this;
    }

    /**
     * Resolves and applies styles for all tiles with actions.
     *
     * @return This builder for chaining.
     */
    public Builder resolveActionStyles() {
      game.getBoard().getTiles().forEach((id, tile) -> {
        tile.getAction().ifPresent(action -> TileStyleService.applyStyle(tile, action, gameBoard));
      });
      return this;
    }

    /**
     * Adds players to the GameBoard.
     *
     * @param players The players to add.
     * @return This builder for chaining.
     */
    public Builder addPlayers(List<Player> players) {
      players.forEach(gameBoard::addPlayer);
      return this;
    }

    /**
     * Finalizes the GameBoard by ensuring the overlay spans everything.
     *
     * @return The fully built GameBoard.
     */
    public GameBoard build() {
      gameBoard.spanOverlay();
      return gameBoard;
    }
  }
}