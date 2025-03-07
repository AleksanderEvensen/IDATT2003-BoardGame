package edu.ntnu.idi.idatt.boardgame.core.reactivity;

public class ObserverEntityB implements Observer<ObservableEntity, Integer> {
  public int counter = 0;
  @Override
  public void update(Integer value) {
    counter = value;
  }
}
