package edu.ntnu.idi.idatt.boardgame.javafx.components;

import edu.ntnu.idi.idatt.boardgame.Utils;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class Button extends javafx.scene.control.Button {

  public enum ButtonVariant {
    PRIMARY, SECONDARY, DESTRUCTIVE, OUTLINE, SUCCESS;

    static Set<String> AllClasses = Arrays.stream(ButtonVariant.values())
        .map(ButtonVariant::getCssClass).collect(Collectors.toSet());

    public String getCssClass() {
      return String.format("Button-variant-%s", name().toLowerCase());
    }
  }

  public Button(String text) {
    super(text);
    getStyleClass().addAll("Button", ButtonVariant.PRIMARY.getCssClass());
  }

  public Button(String text, Ikon icon) {
    this(text);
    this.setGraphic(new FontIcon(icon));
  }

  public Button(Ikon icon) {
    this(null, icon);
  }

  public Button() {
    this("");
  }

  public Button withVariant(ButtonVariant variant) {
    Utils.ensureOneOfClasses(this, variant.getCssClass(), ButtonVariant.AllClasses);
    return this;
  }
}
