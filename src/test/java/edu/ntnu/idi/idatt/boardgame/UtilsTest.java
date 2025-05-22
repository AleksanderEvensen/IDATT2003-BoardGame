package edu.ntnu.idi.idatt.boardgame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.idatt.boardgame.model.entities.Color;
import java.util.List;
import java.util.Set;
import javafx.scene.Node;
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

  @Test
  void throwDice_zeroDice_shouldReturnEmptyList() {
    // Act
    List<Integer> result = Utils.throwDice(0);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void throwDice_negativeDice_shouldReturnEmptyList() {
    // Act
    List<Integer> result = Utils.throwDice(-5);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void getRandomNumber_minGreaterThanMax_shouldThrowIllegalArgumentException() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> Utils.getRandomNumber(10, 5));
  }

  @Test
  void getRandomNumber_validRange_shouldReturnNumberWithinRange() {
    // Act
    int result = Utils.getRandomNumber(1, 10);

    // Assert
    assertTrue(result >= 1 && result <= 10);
  }

  @Test
  void toJFXColor_validModelColor_shouldReturnEquivalentJFXColor() {
    // Arrange
    Color modelColor =
        new Color(255, 0, 0);

    // Act
    javafx.scene.paint.Color jfxColor = Utils.toJFXColor(modelColor);

    // Assert
    assertEquals(1.0, jfxColor.getRed(), 0.01);
    assertEquals(0.0, jfxColor.getGreen(), 0.01);
    assertEquals(0.0, jfxColor.getBlue(), 0.01);
  }

  @Test
  void toModelColor_validJFXColor_shouldReturnEquivalentModelColor() {
    // Arrange
    javafx.scene.paint.Color jfxColor = javafx.scene.paint.Color.rgb(0, 255, 0);

    // Act
    Color modelColor = Utils.toModelColor(jfxColor);

    // Assert
    assertEquals(0, modelColor.r);
    assertEquals(255, modelColor.g);
    assertEquals(0, modelColor.b);
  }

  @Test
  void ensureOneOfClasses_shouldEnableSpecifiedClassAndRemoveOthers() {
    // Arrange
    Node node = new javafx.scene.layout.Pane();
    node.getStyleClass().addAll("class1", "class2", "class3");
    Set<String> classSet = Set.of("class1", "class2", "class3");

    // Act
    Utils.ensureOneOfClasses(node, "class2", classSet);

    // Assert
    assertTrue(node.getStyleClass().contains("class2"));
    assertFalse(node.getStyleClass().contains("class1"));
    assertFalse(node.getStyleClass().contains("class3"));
  }
}