package edu.ntnu.idi.idatt.boardgame.router;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import edu.ntnu.idi.idatt.boardgame.core.router.NavigationContext;
import edu.ntnu.idi.idatt.boardgame.core.router.NavigationException;
import edu.ntnu.idi.idatt.boardgame.core.router.Router;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class RouterTest {

  private Router<String> router;
  private Consumer<NavigationContext<String>> navigationHandler;

  @BeforeEach
  @SuppressWarnings("unchecked")
  void setUp() {
    navigationHandler = mock(Consumer.class);
    router = new Router<>(navigationHandler);

    // Setup some test routes
    router.createRoute("/home", "HomeView")
        .createRoute("/games", "GamesView")
        .createRoute("/game/:gameId", "GameView")
        .createRoute("/player/:playerId/stats", "PlayerStatsView")
        .createRoute("/settings", "SettingsView");
  }

  @Test
  void createRoute_shouldAddRouteAndReturnRouter() {
    Router<String> result = router.createRoute("/new-route", "NewView");
    assertSame(router, result);
    assertTrue(router.navigate("/new-route"));
  }

  @Test
  void navigate_withExactMatch_shouldCallHandlerAndReturnTrue() {
    boolean result = router.navigate("/home");

    assertTrue(result);

    ArgumentCaptor<NavigationContext<String>> contextCaptor =
        ArgumentCaptor.forClass(NavigationContext.class);
    verify(navigationHandler).accept(contextCaptor.capture());

    NavigationContext<String> capturedContext = contextCaptor.getValue();
    assertEquals("/home", capturedContext.getTemplateUrl());
    assertEquals("/home", capturedContext.getUrl());
    assertTrue(capturedContext.getParams().isEmpty());
  }

  @Test
  void navigate_withParameters_shouldExtractParamsAndCallHandler() {
    boolean result = router.navigate("/game/chess");

    assertTrue(result);

    ArgumentCaptor<NavigationContext<String>> contextCaptor =
        ArgumentCaptor.forClass(NavigationContext.class);
    verify(navigationHandler).accept(contextCaptor.capture());

    NavigationContext<String> capturedContext = contextCaptor.getValue();
    assertEquals("/game/:gameId", capturedContext.getTemplateUrl());
    assertEquals("/game/chess", capturedContext.getUrl());
    assertEquals("GameView", capturedContext.getData());

    Map<String, String> expectedParams = Map.of("gameId", "chess");
    assertEquals(expectedParams, capturedContext.getParams());
  }

  @Test
  void navigate_withMultipleParameters_shouldExtractAllParams() {
    boolean result = router.navigate("/player/123/stats");

    assertTrue(result);

    ArgumentCaptor<NavigationContext<String>> contextCaptor =
        ArgumentCaptor.forClass(NavigationContext.class);
    verify(navigationHandler).accept(contextCaptor.capture());

    NavigationContext<String> capturedContext = contextCaptor.getValue();
    assertEquals("PlayerStatsView", capturedContext.getData());

    Map<String, String> expectedParams = Map.of("playerId", "123");
    assertEquals(expectedParams, capturedContext.getParams());
  }

  @Test
  void navigate_nonExistentRoute_shouldReturnFalse() {
    boolean result = router.navigate("/non-existent-route");

    assertFalse(result);
    verify(navigationHandler, never()).accept(any());
  }

  @Test
  void navigate_withErrorInHandler_shouldReturnFalse() {
    doThrow(new RuntimeException("Test exception")).when(navigationHandler).accept(any());

    assertThrows(NavigationException.class, () -> router.navigate("/home"));
  }

  @Test
  void refresh_withCurrentContext_shouldNavigateToSameUrlAndReturnTrue() {
    // First navigate somewhere to set current context
    router.navigate("/game/chess");

    // Reset mock to clear previous invocations
    reset(navigationHandler);

    boolean result = router.refresh();

    assertTrue(result);

    ArgumentCaptor<NavigationContext<String>> contextCaptor =
        ArgumentCaptor.forClass(NavigationContext.class);
    verify(navigationHandler).accept(contextCaptor.capture());

    NavigationContext<String> capturedContext = contextCaptor.getValue();
    assertEquals("/game/chess", capturedContext.getUrl());
  }

  @Test
  void refresh_withoutCurrentContext_shouldReturnFalse() {
    // Create a new router without navigating first
    Router<String> freshRouter = new Router<>(navigationHandler);

    boolean result = freshRouter.refresh();

    assertFalse(result);
    verify(navigationHandler, never()).accept(any());
  }

  @Test
  void matchUrlTemplate_exactMatch_shouldReturnEmptyParamsMap() {
    Map<String, String> params = router.matchUrlTemplate("/home", "/home");

    assertNotNull(params);
    assertTrue(params.isEmpty());
  }

  @Test
  void matchUrlTemplate_withParameter_shouldExtractParam() {
    Map<String, String> params = router.matchUrlTemplate("/game/:gameId", "/game/monopoly");

    assertNotNull(params);
    assertEquals(1, params.size());
    assertEquals("monopoly", params.get("gameId"));
  }

  @Test
  void matchUrlTemplate_withMultipleParameters_shouldExtractAllParams() {
    Map<String, String> params = router.matchUrlTemplate(
        "/category/:categoryId/game/:gameId",
        "/category/strategy/game/chess");

    assertNotNull(params);
    assertEquals(2, params.size());
    assertEquals("strategy", params.get("categoryId"));
    assertEquals("chess", params.get("gameId"));
  }

  @Test
  void matchUrlTemplate_differentPatternLength_shouldReturnNull() {
    Map<String, String> params = router.matchUrlTemplate("/home", "/home/subpath");

    assertNull(params);
  }

  @Test
  void matchUrlTemplate_nonMatchingParts_shouldReturnNull() {
    Map<String, String> params = router.matchUrlTemplate("/home/fixed", "/home/different");

    assertNull(params);
  }

  @Test
  void matchUrlTemplate_emptyUrlPart_shouldReturnNull() {
    Map<String, String> params = router.matchUrlTemplate("/home/:section", "/home/");

    assertNull(params);
  }

  @Test
  void getCurrentContext_afterNavigation_shouldReturnCurrentContext() {
    router.navigate("/settings");

    NavigationContext<String> context = router.getCurrentContext();

    assertNotNull(context);
    assertEquals("/settings", context.getUrl());
    assertEquals("SettingsView", context.getData());
  }

  @Test
  void getCurrentContext_beforeNavigation_shouldReturnNull() {
    // Create a new router without navigating
    Router<String> freshRouter = new Router<>(navigationHandler);

    NavigationContext<String> context = freshRouter.getCurrentContext();

    assertNull(context);
  }
}
