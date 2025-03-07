package edu.ntnu.idi.idatt.boardgame.core.reactivity;

public class ObservableEntity extends Observable<ObservableEntity, Integer> {
    private Integer value;

    public ObservableEntity(Integer value) {
        this.value = value;
    }

    public void setValue(Integer value) {
        this.value = value;
        notifyObservers(value);
    }

    public Integer getValue() {
        return value;
    }

}
