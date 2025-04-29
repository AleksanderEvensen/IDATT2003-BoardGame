package edu.ntnu.idi.idatt.boardgame.ui.javafx.components.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;

public enum Weight {
    LIGHT, SEMIBOLD, NORMAL, BOLD;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public String prefixed(String prefix) {
        return String.format("%s-%s", prefix, this.toString());
    }

    public static Set<String> AllPrefixed(@NonNull String prefix) {
        return Arrays.stream(Weight.values()).map(v -> {
            return v.prefixed(prefix);
        }).collect(Collectors.toSet());
    }
}
