package edu.ntnu.idi.idatt.boardgame;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {

    /**
     * Simulates throwing a given amount of six-sided dice.
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

    // Java Doesnt support import aliasing so the two methods below look a bit uggly and unreadable

    public static javafx.scene.paint.Color toJFXColor(
            edu.ntnu.idi.idatt.boardgame.model.Color color) {
        return javafx.scene.paint.Color.rgb(color.r, color.g, color.b);
    }

    public static edu.ntnu.idi.idatt.boardgame.model.Color toModelColor(
            javafx.scene.paint.Color color) {
        return new edu.ntnu.idi.idatt.boardgame.model.Color((int) (color.getRed() * 255),
                (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }

}
