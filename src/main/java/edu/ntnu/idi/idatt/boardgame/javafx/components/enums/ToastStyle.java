package edu.ntnu.idi.idatt.boardgame.javafx.components.enums;

public enum ToastStyle {
  INFO("toast-info"),
  SUCCESS("toast-success"),
  ERROR("toast-error");

  private final String style;

  ToastStyle(String style) {
    this.style = style;
  }

  @Override
  public String toString() {
    return this.style;
  }
}
