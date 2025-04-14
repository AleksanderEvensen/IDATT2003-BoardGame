package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * A customizable tile component for JavaFX-based board games.
 *
 * <p>Features:
 * <ul>
 *   <li>Customizable size, background color, and corner radii.</li>
 *   <li>Optional icon display with rotation.</li>
 *   <li>Hover and click support via handlers.</li>
 *   <li>Ability to toggle highlight and display an ID label.</li>
 * </ul>
 */
public class TileComponent extends StackPane {

  /** Functional interface for handling tile click events. */
  @FunctionalInterface
  public interface TileClickHandler {
    void onTileClicked(TileComponent tile);
  }

  // Default styling
  private static final double DEFAULT_WIDTH = 60.0;
  private static final double DEFAULT_HEIGHT = 60.0;
  private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
  private static final Color DEFAULT_HIGHLIGHT_COLOR = Color.GOLD;
  private static final Color DEFAULT_BORDER_COLOR = Color.DARKGRAY;
  private static final double DEFAULT_BORDER_WIDTH = 1.5;
  private static final double DEFAULT_CORNER_RADIUS = 5.0;
  private static final Font DEFAULT_FONT = Font.font("Arial", 12);

  // Fields
  private int tileId;
  private boolean isHighlighted;
  private boolean isSelectable;
  private Color backgroundColor;
  private Color highlightColor;
  private final Rectangle background;
  private final Label idLabel;
  private FontIcon icon;
  private TileClickHandler clickHandler;

  /**
   * Constructs a tile component with a specified ID.
   *
   * @param tileId The integer ID of the tile.
   */
  public TileComponent(int tileId) {
    this(tileId, null);
  }

  /**
   * Constructs a tile component with a specified ID and optional icon.
   *
   * @param tileId    The integer ID of the tile.
   * @param iconCode  The Ikon code for the icon (e.g., from FontAwesome, Material, etc.).
   */
  public TileComponent(int tileId, Ikon iconCode) {
    this.tileId = tileId;
    this.isHighlighted = false;
    this.isSelectable = true;
    this.backgroundColor = DEFAULT_BACKGROUND_COLOR;
    this.highlightColor = DEFAULT_HIGHLIGHT_COLOR;

    background = new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    background.setFill(backgroundColor);
    background.setStroke(DEFAULT_BORDER_COLOR);
    background.setStrokeWidth(DEFAULT_BORDER_WIDTH);
    background.setArcWidth(DEFAULT_CORNER_RADIUS);
    background.setArcHeight(DEFAULT_CORNER_RADIUS);

    idLabel = new Label(String.valueOf(tileId));
    idLabel.setFont(DEFAULT_FONT);
    idLabel.setTextFill(Color.BLACK);
    StackPane.setAlignment(idLabel, Pos.TOP_RIGHT);

    if (iconCode != null) {
      setIcon(iconCode);
    }

    getChildren().addAll(background, idLabel);
    setAlignment(Pos.CENTER);
    setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    setupMouseHandlers();
  }

  /**
   * Internal method to handle mouse events like hover and click.
   */
  private void setupMouseHandlers() {
    // Hover effect
    setOnMouseEntered(e -> {
      if (isSelectable && !isHighlighted) {
        background.setFill(backgroundColor.brighter());
        setCursor(Cursor.HAND);
      }
    });

    setOnMouseExited(e -> {
      if (!isHighlighted) {
        background.setFill(backgroundColor);
      }
      setCursor(Cursor.DEFAULT);
    });

    // Click event
    setOnMouseClicked(this::handleMouseClick);
  }

  /**
   * Invokes the click handler if tile is selectable.
   */
  private void handleMouseClick(MouseEvent event) {
    if (isSelectable && clickHandler != null) {
      clickHandler.onTileClicked(this);
    }
  }

  /**
   * Sets the tile's icon with no rotation.
   *
   * @param iconCode The Ikon code to use (e.g., FontAwesome, Material, etc.).
   */
  public void setIcon(Ikon iconCode) {
    setIcon(iconCode, 0.0);
  }

  /**
   * Sets the tile's icon with a given rotation angle.
   *
   * @param iconCode The Ikon code to use.
   * @param rotation The rotation angle in degrees.
   */
  public void setIcon(Ikon iconCode, double rotation) {
    if (iconCode != null) {
      // Remove the old icon if present
      if (icon != null) {
        getChildren().remove(icon);
      }
      icon = FontIcon.of(iconCode, 25, Color.BLACK);
      icon.setRotate(rotation);
      getChildren().add(icon);
    }
  }

  /**
   * Sets the background color of the tile.
   *
   * @param color The color to apply.
   */
  public void setBackgroundColor(Color color) {
    this.backgroundColor = color;
    if (!isHighlighted) {
      background.setFill(color);
    }
  }

  /**
   * Sets the highlight color used for the tile.
   *
   * @param color The color to use for highlighting.
   */
  public void setHighlightColor(Color color) {
    this.highlightColor = color;
  }

  /**
   * Toggles the highlight on or off for the tile.
   *
   * @param highlight true to highlight; false to remove highlight.
   */
  public void setHighlighted(boolean highlight) {
    this.isHighlighted = highlight;
    if (highlight) {
      background.setFill(highlightColor);
      background.setStroke(Color.BLACK);
      background.setStrokeWidth(DEFAULT_BORDER_WIDTH + 1.0);
    } else {
      background.setFill(backgroundColor);
      background.setStroke(DEFAULT_BORDER_COLOR);
      background.setStrokeWidth(DEFAULT_BORDER_WIDTH);
    }
  }

  /**
   * Changes the border stroke color of the tile.
   *
   * @param strokeColor New stroke color.
   */
  public void setBorderColor(Color strokeColor) {
    background.setStroke(strokeColor);
  }

  /**
   * Changes the border stroke width of the tile.
   *
   * @param width New stroke width.
   */
  public void setBorderWidth(double width) {
    background.setStrokeWidth(width);
  }

  /**
   * Changes the corner radius for the tile.
   *
   * @param radius The new corner radius.
   */
  public void setCornerRadius(double radius) {
    background.setArcWidth(radius);
    background.setArcHeight(radius);
  }

  /**
   * Updates the tile dimensions.
   *
   * @param width  The new width of the tile.
   * @param height The new height of the tile.
   */
  public void setTileSize(double width, double height) {
    setPrefSize(width, height);
    background.setWidth(width);
    background.setHeight(height);
  }

  /**
   * Shows or hides the tile's ID label.
   *
   * @param visible true if the label should be visible; false otherwise.
   */
  public void showId(boolean visible) {
    idLabel.setVisible(visible);
  }

  /**
   * Updates the ID on the tile (also updates the label text).
   *
   * @param newId The new ID to set.
   */
  public void setTileId(int newId) {
    this.tileId = newId;
    idLabel.setText(String.valueOf(newId));
  }

  /**
   * Changes the font used for the ID label.
   *
   * @param font The new font to apply.
   */
  public void setIdFont(Font font) {
    this.idLabel.setFont(font);
  }

  /**
   * Changes the text color for the ID label.
   *
   * @param color The color to apply to the label text.
   */
  public void setIdTextColor(Color color) {
    this.idLabel.setTextFill(color);
  }

  /**
   * Sets whether the tile can be clicked or not.
   *
   * @param selectable true if clickable; false otherwise.
   */
  public void setSelectable(boolean selectable) {
    this.isSelectable = selectable;
    setOpacity(selectable ? 1.0 : 0.6);
  }

  /**
   * Attaches a click handler to this tile.
   *
   * @param handler A TileClickHandler that handles clicks.
   */
  public void setOnTileClicked(TileClickHandler handler) {
    this.clickHandler = handler;
  }

  /**
   * @return the tile ID
   */
  public int getTileId() {
    return tileId;
  }

  /**
   * @return true if the tile is highlighted
   */
  public boolean isHighlighted() {
    return isHighlighted;
  }

  /**
   * @return true if the tile can be selected (clicked).
   */
  public boolean isSelectable() {
    return isSelectable;
  }
}
