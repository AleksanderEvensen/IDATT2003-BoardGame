package edu.ntnu.idi.idatt.boardgame.router;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import lombok.Getter;

/**
 * Router class for handling URL routing and navigation
 */
public class Router<T> {

  private final Map<String, T> routes = new HashMap<>();
  @Getter
  private NavigationContext<T> currentContext;
  private final Consumer<NavigationContext<T>> navigationHandler;
  private final Logger logger = Logger.getLogger(Router.class.getName());

  public Router(Consumer<NavigationContext<T>> navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  public Router<T> createRoute(String urlTemplate, T routeData) {
    routes.put(urlTemplate, routeData);
    return this;
  }

  /**
   * Navigates to the specified URL
   *
   * @param url The URL to navigate to
   * @return if navigation was successful
   */
  public boolean navigate(String url) {

    for (var entry : routes.entrySet()) {
      String template = entry.getKey();
      Map<String, String> params = matchUrlTemplate(template, url);

      if (params == null) {
        continue;
      }

      try {
        var ctx = new NavigationContext<>(template, url, params, entry.getValue());
        this.navigationHandler.accept(ctx);

        this.currentContext = ctx;

        return true;
      } catch (Exception e) {
        logger.warning("Error while navigating to " + url + ": " + e.getMessage());
        throw new NavigationException(e.getMessage());
      }
      return false;
    }
    return false;
  }

  /**
   * Refreshes the current page.
   *
   * @return True if the page was refreshed, false otherwise
   */
  public boolean refresh() {
    if (currentContext == null) {
      return false;
    }
    return navigate(currentContext.getUrl());
  }

  /**
   * Matches a URL against a template and extracts the parameters.
   *
   * @param template The URL template with parameters denoted by ':paramName'
   * @param url      The URL to test against the template
   * @return A map of parameters if the URL matches the template, null otherwise
   */
  public Map<String, String> matchUrlTemplate(String template, String url) {
    String[] templateParts = template.split("/");
    String[] urlParts = url.split("/");

    if (templateParts.length != urlParts.length) {
      return null;
    }

    Map<String, String> params = new HashMap<>();

    for (int i = 0; i < templateParts.length; i++) {
      String part = templateParts[i];
      String urlPart = urlParts[i];
      String paramName = part.startsWith(":") ? part.substring(1) : null;

      if (paramName != null && !urlPart.isEmpty()) {
        params.put(paramName, urlPart);
        continue;
      }

      if (part.equals(urlPart)) {
        continue;
      }

      return null;

    }

    return params;
  }

}
