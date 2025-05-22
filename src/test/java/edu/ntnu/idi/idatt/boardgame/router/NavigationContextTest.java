package edu.ntnu.idi.idatt.boardgame.router;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.idatt.boardgame.core.router.NavigationContext;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class NavigationContextTest {

  @Test
  void getParam_existingKey_shouldReturnValue() {
    // Arrange
    Map<String, String> params = Map.of("key1", "value1", "key2", "value2");
    NavigationContext<Object> context = new NavigationContext<>("template", "url", params, null);

    // Act
    Optional<String> result = context.getParam("key1");

    // Assert
    assertTrue(result.isPresent());
    assertEquals("value1", result.get());
  }

  @Test
  void getParam_nonExistingKey_shouldReturnEmptyOptional() {
    // Arrange
    Map<String, String> params = Map.of("key1", "value1");
    NavigationContext<Object> context = new NavigationContext<>("template", "url", params, null);

    // Act
    Optional<String> result = context.getParam("key2");

    // Assert
    assertFalse(result.isPresent());
  }

  @Test
  void getParamOrThrow_existingKey_shouldReturnValue() {
    // Arrange
    Map<String, String> params = Map.of("key1", "value1");
    NavigationContext<Object> context = new NavigationContext<>("template", "url", params, null);

    // Act
    String result = context.getParamOrThrow("key1");

    // Assert
    assertEquals("value1", result);
  }

  @Test
  void getParamOrThrow_nonExistingKey_shouldThrowException() {
    // Arrange
    Map<String, String> params = Map.of("key1", "value1");
    NavigationContext<Object> context = new NavigationContext<>("template", "url", params, null);

    // Act & Assert
    assertThrows(NoSuchElementException.class, () -> context.getParamOrThrow("key2"));
  }

  @Test
  void constructor_shouldInitializeFieldsCorrectly() {
    // Arrange
    Map<String, String> params = Map.of("key1", "value1");
    String templateUrl = "template";
    String url = "url";
    Object data = new Object();

    // Act
    NavigationContext<Object> context = new NavigationContext<>(templateUrl, url, params, data);

    // Assert
    assertEquals(templateUrl, context.getTemplateUrl());
    assertEquals(url, context.getUrl());
    assertEquals(params, context.getParams());
    assertEquals(data, context.getData());
  }
}