package edu.ntnu.idi.idatt.boardgame;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ApplicationTest {

  @Test
  void start() {
    assertDoesNotThrow(() -> {
      Application.main(new String[]{});
    });
  }
}