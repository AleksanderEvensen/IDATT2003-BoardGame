package edu.ntnu.idi.idatt.boardgame.core.filesystem;

/**
 * Represents an exception that occurs when saving a file.
 */
public class FileSaveException extends RuntimeException {

  public FileSaveException(String message) {
    super(message);
  }
}
