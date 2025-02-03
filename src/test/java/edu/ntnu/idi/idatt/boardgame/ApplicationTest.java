package edu.ntnu.idi.idatt.boardgame;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ApplicationTest {

  @Test
  void CreatesApplicationWithNoExceptions() {
    assertDoesNotThrow(() -> {
      var app = new Application();
    });
  }
}