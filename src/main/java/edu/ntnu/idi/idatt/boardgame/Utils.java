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

    /**
     * Returns a string from array of bytes
     */
    public static String bytesToString(byte[] bytes) {
        return new String(bytes);
    }

}
