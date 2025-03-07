package edu.ntnu.idi.idatt.boardgame.core.reactivity;



/*
  * An observer interface for the observer pattern.
  *
  * @param <T> the type of the observable value.
  * @param <K> the type of the value that is passed to the observer.
 */
public interface Observer<T, K> {

  /**
   * Updates the observer with a new value.
   *
   * @param value the new value.
   */
  void update(K value);
}
