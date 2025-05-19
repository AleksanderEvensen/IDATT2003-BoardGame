package edu.ntnu.idi.idatt.boardgame.router;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.Getter;

/**
 * A class that represents the context of navigation. It contains information about the template
 * URL, the URL that was navigated to, the parameters of the URL, and any associated data.
 *
 * @param <T> the type of data associated with the navigation context
 * @version v1.0.0
 * @since v3.0.0
 */
public class NavigationContext<T> {

  @Getter
  private final String templateUrl;
  @Getter
  private final String url;
  @Getter
  private final Map<String, String> params;
  @Getter
  private final T data;

  /**
   * Creates a new NavigationContext
   *
   * @param templateUrl the template url that matched the url
   * @param url         the url that was navigated to
   * @param params      the parameters of the url
   * @param data        the data associated with the url
   */
  public NavigationContext(String templateUrl, String url, Map<String, String> params, T data) {
    this.templateUrl = templateUrl;
    this.url = url;
    this.params = params;
    this.data = data;
  }

  /**
   * Returns the value of the url parameter with the specified key.
   *
   * @param key the key to look for in the params
   * @return an Optional containing the value of the url parameter with the specified key if it
   * exists.
   */
  public Optional<String> getParam(String key) {
    return Optional.ofNullable(params.get(key));
  }

  /**
   * Returns the value of the url parameter with the specified key.
   *
   * @param key the key to look for in the params
   * @return the value of the url parameter with the specified key
   * @throws NoSuchElementException if the specified key does not exist in the params
   */
  public String getParamOrThrow(String key) {
    return getParam(key).orElseThrow();
  }
}
