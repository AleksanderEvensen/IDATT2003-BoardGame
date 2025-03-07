package edu.ntnu.idi.idatt.boardgame.core.reactivity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObservableTest {
    private ObservableEntity observable;
    private ObserverEntityA observerA;
    private ObserverEntityB observerB;

    @BeforeEach
    public void setUp() {
        observable = new ObservableEntity(0);
        observerA = new ObserverEntityA();
        observerB = new ObserverEntityB();
    }

    @Test
    void testAddListener() {
        observable.addListener(observerA);
        observable.setValue(10);

        assertEquals(1, observable.getObserverCount());
        assertEquals(10, observerA.counter);
    }

    @Test
    void testRemoveListener() {
        observable.addListener(observerA);
        observable.removeListener(observerA);
        observable.setValue(10);

        assertEquals(0, observable.getObserverCount());
        assertEquals(0, observerA.counter);
    }

    @Test
    void testNotifyObservers() {
        observable.addListener(observerA);
        observable.addListener(observerB);
        observable.setValue(20);

        assertEquals(2, observable.getObserverCount());
        assertEquals(20, observerA.counter);
        assertEquals(20, observerB.counter);
    }

    @Test
    void testClearObservers() {
        observable.addListener(observerA);
        observable.addListener(observerB);
        observable.clearObservers();

        assertEquals(0, observable.getObserverCount());
    }

    @Test
    void testGetObserverCount() {
        observable.addListener(observerA);
        observable.addListener(observerB);

        assertEquals(2, observable.getObserverCount());
    }

    @Test
    void testGetObservers() {
        observable.addListener(observerA);
        observable.addListener(observerB);

        assertEquals(2, observable.getObservers().size());
    }
}