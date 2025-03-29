package edu.ntnu.idi.idatt.boardgame.ui.javafx.style;

import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.LadderComponent;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.ui.style.LadderTileStyle;
import edu.ntnu.idi.idatt.boardgame.ui.style.TileStyleApplier;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;

/**
 * JavaFX-specific implementation of the ladder tile style applier.
 * This class applies visual styling to tiles with ladder actions and draws the ladder between them.
 *
 * @see TileStyleApplier
 * @since v2.0.0
 */
public class JavaFxLadderStyleApplier implements TileStyleApplier {

    private static final int DEFAULT_RUNGS_PER_100_PIXELS = 3;
    private static final double MIN_RUNGS = 2;
    private static final double MAX_RUNGS = 10;

    @Override
    public void applyStyle(Tile tile, TileAction action, Object parent) {
        if (!(parent instanceof GameBoard) || !(action instanceof LadderAction)) {
            return;
        }

        GameBoard gameBoard = (GameBoard) parent;
        LadderAction ladderAction = (LadderAction) action;

        TileComponent startTile = findTileComponent(gameBoard, tile.getTileId());
        TileComponent destinationTile = findTileComponent(gameBoard, ladderAction.getDestinationTile().getTileId());

        if (startTile == null || destinationTile == null) {
            System.err.println("Could not find tile components for ladder from " +
                tile.getTileId() + " to " + ladderAction.getDestinationTile().getTileId());
            return;
        }

        boolean isPositive = ladderAction.getDestinationTile().getTileId() > tile.getTileId();
        applyTileColors(startTile, destinationTile, isPositive);


        Platform.runLater(() -> {
            gameBoard.applyCss();
            gameBoard.layout();

            Bounds startBounds = startTile.localToScene(startTile.getBoundsInLocal());
            Bounds overlayBounds = gameBoard.getOverlayPane().sceneToLocal(startBounds);
            double startX = overlayBounds.getMinX() + overlayBounds.getWidth() / 2;
            double startY = overlayBounds.getMinY() + overlayBounds.getHeight() / 2;

            Bounds endBounds = destinationTile.localToScene(destinationTile.getBoundsInLocal());
            Bounds endOverlayBounds = gameBoard.getOverlayPane().sceneToLocal(endBounds);
            double endX = endOverlayBounds.getMinX() + endOverlayBounds.getWidth() / 2;
            double endY = endOverlayBounds.getMinY() + endOverlayBounds.getHeight() / 2;

            double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
            int rungs = calculateRungCount(distance);

            LadderComponent ladderComponent = new LadderComponent(startX, startY, endX, endY, rungs);



            gameBoard.getOverlayPane().getChildren().add(ladderComponent);
        });
    }

    /**
     * Finds a TileComponent by its ID in the GameBoard.
     *
     * @param gameBoard The game board containing the tiles
     * @param tileId The ID of the tile to find
     * @return The found TileComponent, or null if not found
     */
    private TileComponent findTileComponent(GameBoard gameBoard, int tileId) {
        return gameBoard.getChildren().stream()
            .filter(node -> node instanceof TileComponent)
            .map(node -> (TileComponent) node)
            .filter(tileComponent -> tileComponent.getTileId() == tileId)
            .findFirst()
            .orElse(null);
    }

    /**
     * Applies appropriate colors to start and destination tiles based on ladder direction.
     *
     * @param startTile The starting tile
     * @param destinationTile The destination tile
     * @param isPositive Whether the ladder goes up (true) or down (false)
     */
    private void applyTileColors(TileComponent startTile, TileComponent destinationTile, boolean isPositive) {
        if (isPositive) {
            startTile.setBackgroundColor(Color.valueOf(LadderTileStyle.START_POSITIVE_COLOR));
            destinationTile.setBackgroundColor(Color.valueOf(LadderTileStyle.POSITIVE_COLOR));
        } else {
            startTile.setBackgroundColor(Color.valueOf(LadderTileStyle.START_NEGATIVE_COLOR));
            destinationTile.setBackgroundColor(Color.valueOf(LadderTileStyle.NEGATIVE_COLOR));
        }
    }

    /**
     * Calculates an appropriate number of rungs based on ladder length.
     *
     * @param distance The distance between the start and end points
     * @return The number of rungs to draw
     */
    private int calculateRungCount(double distance) {
        int calculatedRungs = (int) (distance * DEFAULT_RUNGS_PER_100_PIXELS / 100);
        return (int) Math.max(MIN_RUNGS, Math.min(calculatedRungs, MAX_RUNGS));
    }
}