package edu.ntnu.idi.idatt.boardgame.ui.javafx.player;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.PlayerBlipView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Extension of Player that includes JavaFX-specific visualization properties.
 * <p>
 * This class allows for customization of the player's appearance on the game
 * board
 * with configurable colors and pawn icons.
 * </p>
 */
public class JavaFXPlayer extends Player {

  private static final Logger logger = Logger.getLogger(JavaFXPlayer.class.getName());
  private static final Color DEFAULT_COLOR = Color.LIGHTBLUE;

  private Color color;
  private String pawnImageName;

  /**
   * Constructs a player with the specified ID, name, and default appearance.
   *
   * @param playerId the player's ID
   * @param name     the player's name
   */
  public JavaFXPlayer(int playerId, String name) {
    super(playerId, name);
    this.color = DEFAULT_COLOR;
    this.pawnImageName = PawnAssetManager.getInstance().getDefaultPawnImageName();
    logger.fine("Created JavaFXPlayer with ID " + playerId + ", name " + name + ", and default appearance");
  }

  /**
   * Constructs a player with the specified ID, name, and custom appearance.
   *
   * @param playerId      the player's ID
   * @param name          the player's name
   * @param color         the color for the player's pawn background
   * @param pawnImageName the filename of the pawn image (from resources/pawns/)
   */
  public JavaFXPlayer(int playerId, String name, Color color, String pawnImageName) {
    super(playerId, name);
    this.color = color;
    this.pawnImageName = pawnImageName;

    if (!PawnAssetManager.getInstance().isPawnImageAvailable(pawnImageName)) {
      logger.log(Level.WARNING, "Invalid pawn image name '{0}', using default", pawnImageName);
      this.pawnImageName = PawnAssetManager.getInstance().getDefaultPawnImageName();
    }

    logger.fine("Created JavaFXPlayer with ID " + playerId + ", name " + name + ", color " + color + ", and pawn "
        + pawnImageName);
  }

  /**
   * Gets the player's pawn background color.
   *
   * @return the background color
   */
  public Color getColor() {
    return color;
  }

  /**
   * Sets the player's pawn background color.
   *
   * @param color the background color to set
   */
  public void setColor(Color color) {
    this.color = color;
    logger.fine("Set color to " + color + " for player " + getName());
  }

  /**
   * Gets the name of the pawn image file.
   *
   * @return the pawn image name
   */
  public String getPawnImageName() {
    return pawnImageName;
  }

  /**
   * Sets the pawn image by filename.
   *
   * @param pawnImageName the filename of the pawn image (from resources/pawns/)
   * @return true if image was valid, false otherwise
   */
  public boolean setPawnImage(String pawnImageName) {

    if (!PawnAssetManager.getInstance().isPawnImageAvailable(pawnImageName)) {
      logger.log(Level.WARNING, "Invalid pawn image name '{0}', not changing", pawnImageName);
      return false;
    }

    this.pawnImageName = pawnImageName;
    logger.fine("Set pawn image to " + pawnImageName + " for player " + getName());
    return true;
  }

  /**
   * Gets the pawn image for display.
   *
   * @return the pawn image
   */
  public Image getPawnImage() {
    return PawnAssetManager.getInstance().getPawnImage(pawnImageName);
  }

  /**
   * Creates a PlayerBlipView to represent this player on the game board.
   *
   * @return a new PlayerBlipView instance
   */
  public PlayerBlipView createBlipView() {
    return new PlayerBlipView(this);
  }
}
