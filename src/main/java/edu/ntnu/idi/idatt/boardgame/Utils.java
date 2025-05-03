package edu.ntnu.idi.idatt.boardgame;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import edu.ntnu.idi.idatt.boardgame.model.Color;
import javafx.scene.Node;

public class Utils {

  /**
   * Simulates throwing a given amount of six-sided dice.
   * 
   * @param amount The amount of dice to throw.
   * @return A list of integers representing the dice rolls.
   */
  public static List<Integer> throwDice(int amount) {
    Random rand = new Random();

    return IntStream.range(0, amount)
        .map(i -> rand.nextInt(6) + 1)
        .boxed()
        .collect(Collectors.toList());
  }

  /**
   * Generates a random number between the given min and max values.
   * 
   * @param min The minimum value (inclusive).
   * @param max The maximum value (inclusive).
   * @return A random integer between min and max.
   */
  public static Integer getRandomNumber(int min, int max) {
    Random rand = new Random();
    return rand.nextInt(max - min + 1) + min;
  }

  // Java Doesnt support import aliasing so the two methods below look a bit uggly
  // and unreadable

  public static javafx.scene.paint.Color toJFXColor(
      edu.ntnu.idi.idatt.boardgame.model.Color color) {
    return javafx.scene.paint.Color.rgb(color.r, color.g, color.b);
  }

  public static edu.ntnu.idi.idatt.boardgame.model.Color toModelColor(
      javafx.scene.paint.Color color) {
    return new edu.ntnu.idi.idatt.boardgame.model.Color((int) (color.getRed() * 255),
        (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
  }

  /**
   * Ensures that a node has only one of the given classes, and removes all other
   * classes.
   * 
   * @param node     the node to modify
   * @param toEnable the class to enable (prefferably a class in the classSet)
   * @param classSet the set of classes to remove
   */
  public static void ensureOneOfClasses(Node node, String toEnable, Set<String> classSet) {
    Set<String> filteredClasses = node.getStyleClass().stream()
        .filter(c -> !classSet.contains(c)).collect(Collectors.toSet());

    if (toEnable != null) {
      filteredClasses.add(toEnable);
    }
    node.getStyleClass().setAll(filteredClasses);
  }

  public static Color getRandomColor() {
    Random rand = new Random();
    return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
  }
}
