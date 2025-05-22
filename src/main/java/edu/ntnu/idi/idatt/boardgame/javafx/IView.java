package edu.ntnu.idi.idatt.boardgame.javafx;

import edu.ntnu.idi.idatt.boardgame.core.router.NavigationContext;
import javafx.scene.layout.StackPane;

public interface IView {

  /*
   * A function for loading the view
   */
  default void load() {
  }

  /*
   * A function for loading the view with a navigation context
   */
  default void load(NavigationContext<?> ctx) {
    load();
  }

  /**
   * A function for unloading the view
   */
  default void unload() {
  }

  /**
   * A function for creating the view
   */
  StackPane createRoot();
}
