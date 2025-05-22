package edu.ntnu.idi.idatt.boardgame.javafx.components;

import edu.ntnu.idi.idatt.boardgame.javafx.components.Header.HeaderType;
import edu.ntnu.idi.idatt.boardgame.model.entities.Game;
import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * A component representing a game card to display info about a game.
 *
 * @since v2.0.0
 */
public class GameCard extends Card {

  private static final Image GAME_FALLBACK_IMAGE = new Image("images/not_found.png");

  public GameCard(Game game) {
    this.getStyleClass().add("GameCard");
    this.setPadding(new Insets(5));

    ImageView gameImageView = new ImageView(GAME_FALLBACK_IMAGE);
    gameImageView.getStyleClass().add("rounded-md");
    game.getImagePath().ifPresent(path -> {
      var file = new File(path);
      if (file.exists()) {
        gameImageView.setImage(new Image(file.toURI().toString()));
      }
    });
    gameImageView.setFitHeight(120);
    gameImageView.setFitWidth(180);
    this.setTop(gameImageView);

    Header gameNameLabel = new Header(game.getName()).withType(HeaderType.H5);

    HBox settingsContainer = new HBox(20);

    Header playerCount = new Header(game.getMinPlayers() + "-" + game.getMaxPlayers())
        .withFontSize(16);
    var playersIcon = new FontIcon(BoxiconsRegular.USER);
    playersIcon.setIconSize(16);
    playerCount.setGraphic(playersIcon);

    Header diceCount = new Header("" + game.getNumberOfDice()).withFontSize(16);
    var diceIcon = new FontIcon(BoxiconsRegular.DICE_6);
    diceIcon.setIconSize(16);
    diceCount.setGraphic(diceIcon);

    settingsContainer.getChildren().addAll(playerCount, diceCount);

    VBox details = new VBox(5);

    details.getChildren().addAll(gameNameLabel, settingsContainer);

    details.setPadding(new Insets(5, 0, 5, 0));

    this.setCenter(details);
  }
}
