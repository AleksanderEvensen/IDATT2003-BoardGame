package edu.ntnu.idi.idatt.boardgame.core.filesystem;

import java.io.InputStream;

/**
 * Interface for providing file storage and retrieval functionality.
 */
public interface FileProvider {

  /**
   * Saves the given data to the specified path.
   *
   * @param path  The file path or identifier where data should be saved.
   * @param data  The InputStream representing the file/data to be saved.
   * @throws      FileSaveException if an error occurs while saving the file.
   */
  void save(String path, InputStream data);

  /**
   * Deletes the file or data at the specified path.
   *
   * @param path  The file path or identifier to delete.
   * @return      true if the file was deleted, false if no file existed at that path.
   * @throws      FileDeleteException if an error occurs while deleting the file.
   */
  boolean delete(String path);

  /**
   * Checks if a file or data exists at the specified path.
   *
   * @param path  The file path or identifier.
   * @return      true if the file exists, false otherwise.
   */
  boolean exists(String path);

  /**
   * Retrieves the file/data as an {@link InputStream}, or returns null if not found.
   *
   * @param path  The file path or identifier to retrieve.
   * @return      An InputStream of the requested file/data, or null if it does not exist.
   * @throws      FileReadException if an error occurs while loading the file.
   */
  InputStream get(String path);
}
