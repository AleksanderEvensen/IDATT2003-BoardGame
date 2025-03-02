package edu.ntnu.idi.idatt.boardgame;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilsTest {

  @BeforeEach
  void setUp() {
  }

  @Test
  void throwDice() {
    // Arrange
    int amount = 5;

    // Act
    List<Integer> result = Utils.throwDice(amount);

    // Assert
    assertNotNull(result);
    assertEquals(amount, result.size());
    for (int roll : result) {
      assertTrue(roll >= 1 && roll <= 6);
    }
  }
}