package edu.ntnu.idi.idatt.boardgame.router;

import java.util.Map;
import java.util.Optional;

public class NavigationContext<T> {

  private final String templateUrl;
  private final String url;
  private final Map<String, String> params;
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

  public String getTemplateUrl() {
    return templateUrl;
  }

  public String getUrl() {
    return url;
  }

  public T getData() {
    return data;
  }

  /**
   * Returns the parameters of the url as a map
   *
   * @return
   */
  public Map<String, String> getParams() {
    return params;
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
