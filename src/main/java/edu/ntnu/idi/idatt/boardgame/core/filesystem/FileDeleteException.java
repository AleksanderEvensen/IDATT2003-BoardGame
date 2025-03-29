package edu.ntnu.idi.idatt.boardgame.core.filesystem;

/**
 * Represents an exception that occurs when deleting a file.
 */
public class FileDeleteException extends RuntimeException {

  public FileDeleteException(String message) {
    super(message);
  }
}
