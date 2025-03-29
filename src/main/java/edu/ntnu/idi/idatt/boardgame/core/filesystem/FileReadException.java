package edu.ntnu.idi.idatt.boardgame.core.filesystem;


/**
 * Represents an exception that occurs when reading a file.
 */
public class FileReadException extends RuntimeException {

  public FileReadException(String message) {
    super(message);
  }
}
