package edu.ntnu.idi.idatt.boardgame.core.filesystem;


/**
 * An exception that is thrown when a file cannot be read.
 */
public class FileReadException extends RuntimeException {

  public FileReadException(String message) {
    super(message);
  }
}
