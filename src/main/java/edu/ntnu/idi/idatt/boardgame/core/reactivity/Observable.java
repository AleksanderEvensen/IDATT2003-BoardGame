package edu.ntnu.idi.idatt.boardgame.core.reactivity;
import java.util.HashSet;
import java.util.Set;

/**
 * An observable class for the observer pattern.
 *
 * @param <T> the type of the observable value.
 * @param <K> the type of the value that is passed to the observer.
 */
public abstract class Observable<T, K> {

  HashSet<Observer<T, K>> observers;

  public Observable() {
    observers = new HashSet<>();
  }

  /**
   * Adds an observer to the observable.
   *
   * @param observer the observer to add.
   */
  public void addListener(Observer<T, K> observer) {
    observers.add(observer);
  }

  /**
   * Removes an observer from the observable.
   *
   * @param observer the observer to remove.
   */
  public void removeListener(Observer<T, K> observer) {
    observers.remove(observer);
  }

  /**
   * Notifies all observers of a change in the observable.
   *
   * @param value the value to pass to the observers.
   */
  public void notifyObservers(K value) {
    observers.forEach(observer -> observer.update(value));
  }

  /**
   * Clears all observers from the observable.
   */
  public void clearObservers() {
    observers.clear();
  }

  /**
   * Returns the number of observers.
   *
   * @return the number of observers.
   */
  public int getObserverCount() {
    return observers.size();
  }

  /**
   * Returns a set of all observers.
   *
   * @return a set of all observers.
   */
  public Set<Observer<T, K>> getObservers() {
    return observers;
  }

}
