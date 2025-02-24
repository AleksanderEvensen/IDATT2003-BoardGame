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
    List<Integer> result = Utils.throwDice(99);
    assertEquals(result.size(), 99);
    assertTrue(result.stream().allMatch(i -> i >= 1 && i <= 6));
  }
}