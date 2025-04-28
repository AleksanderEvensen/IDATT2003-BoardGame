package edu.ntnu.idi.idatt.boardgame.model;

public class Color {

  /// The red component of the color (0-255)
  public final int r;
  /// The green component of the color (0-255)
  public final int g;
  /// The blue component of the color (0-255)
  public final int b;

  /**
   * Constructs a color with the specified RGB values.
   *
   * @param red the red component (0-255)
   * @param green the green component (0-255)
   * @param blue the blue component (0-255)
   * @throws IllegalArgumentException if the RGB values are not in the range 0-255
   */
  public Color(int red, int green, int blue) {
    if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
      throw new IllegalArgumentException("RGB values must be between 0 and 255");
    }
    this.r = red;
    this.g = green;
    this.b = blue;
  }

  /**
   * Returns the color as a hex string in the format #RRGGBB.
   *
   * @return the hex string representation of the color
   */
  public String toHex() {
    return String.format("#%02X%02X%02X", r, g, b);
  }


  /**
   * Creates a Color object from a hex string in the format #RRGGBB.
   *
   * @param hex the hex string representation of the color
   * @return the Color object
   * @throws IllegalArgumentException if the hex string is invalid
   */
  public static Color fromHex(String hex) {
    if (hex.trim().startsWith("#")) {
      hex = hex.trim().substring(1);
    }
    if (hex.length() != 6) {
      throw new IllegalArgumentException("Invalid hex color format, expected: #RRGGBB");
    }

    int r = Integer.parseInt(hex.substring(0, 2), 16);
    int g = Integer.parseInt(hex.substring(2, 4), 16);
    int b = Integer.parseInt(hex.substring(4, 6), 16);
    return new Color(r, g, b);
  }

  public static final Color RED = new Color(255, 0, 0);
  public static final Color GREEN = new Color(0, 255, 0);
  public static final Color BLUE = new Color(0, 0, 255);
  public static final Color YELLOW = new Color(255, 255, 0);
  public static final Color CYAN = new Color(0, 255, 255);
  public static final Color MAGENTA = new Color(255, 0, 255);
  public static final Color BLACK = new Color(0, 0, 0);


  @Override
  public String toString() {
    return String.format("Color{r = %d, g = %d, b = %d}", r, g, b);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof Color))
      return false;
    Color color = (Color) obj;
    return r == color.r && g == color.g && b == color.b;
  }
}
