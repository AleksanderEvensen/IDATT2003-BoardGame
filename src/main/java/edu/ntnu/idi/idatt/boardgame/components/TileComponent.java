package edu.ntnu.idi.idatt.boardgame.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class TileComponent extends StackPane {

  private int id;
  private Rectangle background;
  private Ikon iconView;
  private Color defaultColor = Color.WHITE;
  private Label idLabel = new Label();

  public TileComponent(int id) {
    this(id, null);
  }

  public TileComponent(int id, Ikon icon) {
    this.id = id;

    background = new Rectangle(50, 50);
    background.setFill(defaultColor);
    background.setArcWidth(10);
    background.setArcHeight(10);
    idLabel.setText(String.valueOf(id));
    getChildren().add(background);
    getStyleClass().add("tile");


    iconView = icon;
    if (iconView != null) {
      getChildren().add(FontIcon.of(iconView, 25, Color.WHITE));
      setAlignment(Pos.CENTER);
      setPrefSize(50, 50);
    }

  }

  public void setIcon(Ikon icon) {
     iconView = icon;
  }

  public void setBackgroundColor(Color color) {
    this.defaultColor = color;
    background.setFill(color);
  }

  public int getTileId() {
    return id;
  }

  public void setTileId(int id) {
    this.id = id;
  }

  public void setTileSize(double width, double height) {
    setPrefSize(width, height);
    background.setWidth(width);
    background.setHeight(height);
  }
}