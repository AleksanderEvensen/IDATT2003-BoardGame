package edu.ntnu.idi.idatt.boardgame.javafx.components.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;

public enum Size {
  XS, SM, NORMAL, MD, LG, XL;

  @Override
  public String toString() {
    return this.name().toLowerCase();
  }

  public String prefixed(String prefix) {
    return String.format("%s-%s", prefix, this);
  }

  public static Set<String> AllPrefixed(@NonNull String prefix) {
    return Arrays.stream(Size.values()).map(v -> {
      return v.prefixed(prefix);
    }).collect(Collectors.toSet());
  }
}
