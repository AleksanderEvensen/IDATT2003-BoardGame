package edu.ntnu.idi.idatt.boardgame.core.filesystem;


/**
 * An exception that is thrown when a file cannot be read.
 */
public class FileReadException extends RuntimeException {

  /**
   * Constructs a new FileReadException with the specified detail message.
   *
   * @param message the detail message
   */
  public FileReadException(String message) {
    super(message);
  }
}
