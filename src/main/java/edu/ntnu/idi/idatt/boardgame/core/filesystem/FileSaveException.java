package edu.ntnu.idi.idatt.boardgame.core.filesystem;

/**
 * An exception that is thrown when a file cannot be deleted.
 */
public class FileSaveException extends RuntimeException {

  public FileSaveException(String message) {
    super(message);
  }
}
