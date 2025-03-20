package edu.ntnu.idi.idatt.boardgame.actions;

public interface HasStyleResolver {

  /**
   * Get the style resolver for the action.
   *
   * @return the style resolver.
   */
  TileActionStyleResolver getStyleResolver();

}
