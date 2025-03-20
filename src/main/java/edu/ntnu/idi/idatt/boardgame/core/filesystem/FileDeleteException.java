package edu.ntnu.idi.idatt.boardgame.core.filesystem;

/**
 * An exception that is thrown when a file cannot be deleted.
 */
public class FileDeleteException extends RuntimeException {

  public FileDeleteException(String message) {
    super(message);
  }
}
