package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

import static edu.ntnu.idi.idatt.boardgame.ui.style.TileStyle.TILE_SIZE;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Represents a single tile in a board game.
 * Provides functionality for displaying, highlighting, and interacting with game tiles.
 */
public class TileComponent extends StackPane {

  private int id;
  private Rectangle background;
  private Color defaultColor = Color.WHITE;
  private Color highlightColor = Color.GOLD;
  private final Color borderColor = Color.DARKGRAY;
  private final Label idLabel;
  private FontIcon icon;
  private boolean isHighlighted = false;
  private boolean isSelectable = true;
  private TileClickHandler clickHandler;

  /**
   * Interface for handling tile click events.
   */
  public interface TileClickHandler {
    void onTileClicked(TileComponent tile);
  }

  /**
   * Creates a tile with only an ID.
   *
   * @param id The unique identifier for this tile
   */
  public TileComponent(int id) {
    this(id, null);
  }

  /**
   * Creates a tile with an ID and an icon.
   *
   * @param id The unique identifier for this tile
   * @param iconCode The icon to display on this tile
   */
  public TileComponent(int id, Ikon iconCode) {
    this.id = id;

    background = new Rectangle(TILE_SIZE, TILE_SIZE);
    background.setFill(defaultColor);
    background.setStroke(borderColor);
    background.setStrokeWidth(1.5);
    background.setArcWidth(5);
    background.setArcHeight(5);

    idLabel = new Label(String.valueOf(id));
    idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
    idLabel.setTextFill(Color.BLACK);

    // Add background and label to the StackPane
    getChildren().addAll(background, idLabel);

    // Align the ID label to the top-right corner
    StackPane.setAlignment(idLabel, Pos.TOP_RIGHT);
    // Optionally add a small margin so the text isn't too close to the border
    StackPane.setMargin(idLabel, new Insets(5, 5, 5, 5));

    if (iconCode != null) {
      setIcon(iconCode);
    }

    setAlignment(Pos.CENTER);
    setPrefSize(TILE_SIZE, TILE_SIZE);
    setupMouseHandlers();
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(3.0);
    dropShadow.setOffsetX(1.0);
    dropShadow.setOffsetY(1.0);
    dropShadow.setColor(Color.gray(0.4, 0.5));
    setEffect(dropShadow);
  }

  /**
   * Set up mouse event handlers for hover and click effects.
   */
  private void setupMouseHandlers() {
    setOnMouseEntered(e -> {
      if (isSelectable && !isHighlighted) {
        background.setFill(defaultColor.brighter());
        setCursor(javafx.scene.Cursor.HAND);
      }
    });

    setOnMouseExited(e -> {
      if (!isHighlighted) {
        background.setFill(defaultColor);
      }
      setCursor(javafx.scene.Cursor.DEFAULT);
    });

    setOnMouseClicked(this::handleMouseClick);
  }

  /**
   * Handle mouse click events on the tile.
   *
   * @param event The mouse event
   */
  private void handleMouseClick(MouseEvent event) {
    if (isSelectable && clickHandler != null) {
      clickHandler.onTileClicked(this);
    }
  }

  /**
   * Sets an icon on the tile with a specified rotation angle.
   *
   * @param iconCode The icon to display
   * @param angle The rotation angle in degrees
   */
  public void setIcon(Ikon iconCode, double angle) {
    if (iconCode != null) {
      if (icon != null) {
        getChildren().remove(icon);
      }
      icon = FontIcon.of(iconCode, 25, Color.BLACK);
      icon.setRotate(angle);
      getChildren().add(icon);
    }
  }

  /**
   * Sets an icon on the tile with no rotation.
   *
   * @param iconCode The icon to display
   */
  public void setIcon(Ikon iconCode) {
    setIcon(iconCode, 0);
  }

  /**
   * Changes the background color of the tile.
   *
   * @param color The new background color
   */
  public void setBackgroundColor(Color color) {
    this.defaultColor = color;
    if (!isHighlighted) {
      background.setFill(color);
    }
  }

  /**
   * Toggle the highlight state of this tile.
   *
   * @param highlight true to highlight, false to remove highlight
   */
  public void setHighlighted(boolean highlight) {
    this.isHighlighted = highlight;
    if (highlight) {
      background.setFill(highlightColor);
      background.setStroke(Color.BLACK);
      background.setStrokeWidth(2.5);
    } else {
      background.setFill(defaultColor);
      background.setStroke(borderColor);
      background.setStrokeWidth(1.5);
    }
  }

  /**
   * Gets the ID of this tile.
   *
   * @return The tile ID
   */
  public int getTileId() {
    return id;
  }

  /**
   * Sets the ID of this tile.
   *
   * @param id The new tile ID
   */
  public void setTileId(int id) {
    this.id = id;
    idLabel.setText(String.valueOf(id));
  }

  /**
   * Sets the dimensions of this tile.
   *
   * @param width The width of the tile
   * @param height The height of the tile
   */
  public void setTileSize(double width, double height) {
    setPrefSize(width, height);
    background.setWidth(width);
    background.setHeight(height);
  }

  /**
   * Sets the color used when the tile is highlighted.
   *
   * @param color The highlight color
   */
  public void setHighlightColor(Color color) {
    this.highlightColor = color;
    if (isHighlighted) {
      background.setFill(color);
    }
  }

  /**
   * Sets whether this tile can be selected.
   *
   * @param selectable true if the tile can be selected, false otherwise
   */
  public void setSelectable(boolean selectable) {
    this.isSelectable = selectable;
    setOpacity(selectable ? 1.0 : 0.6);
  }

  /**
   * Sets the click handler for this tile.
   *
   * @param handler The handler to call when this tile is clicked
   */
  public void setOnTileClicked(TileClickHandler handler) {
    this.clickHandler = handler;
  }

  /**
   * Shows or hides the ID label.
   *
   * @param show true to show the label, false to hide it
   */
  public void showId(boolean show) {
    idLabel.setVisible(show);
  }

  /**
   * Checks if this tile is currently highlighted.
   *
   * @return true if highlighted, false otherwise
   */
  public boolean isHighlighted() {
    return isHighlighted;
  }

  /**
   * Checks if this tile is currently selectable.
   *
   * @return true if selectable, false otherwise
   */
  public boolean isSelectable() {
    return isSelectable;
  }
}
