package edu.ntnu.idi.idatt.boardgame.core.filesystem;

/**
 * Interface for providing file storage and retrieval functionality.
 */
public interface FileProvider {

  /**
   * Deletes the file at the specified path.
   * 
   * @param path The file path to delete.
   * @return true if the file was deleted, false if no file existed at that path.
   * 
   * @throws FileDeleteException if an error occurs while deleting the file.
   */
  boolean delete(String path);

  /**
   * Saves the data to the specified path.
   * 
   * @param path The file path to save the data to.
   * @param data The byte array representing the file/data to be saved.
   * 
   * @throws FileSaveException if an error occurs while saving the file.
   */
  void save(String path, byte[] data);

  /**
   * Checks if a file exists at the specified path.
   * 
   * @param path The file path to check.
   * @return true if the file exists, false otherwise.
   */
  boolean exists(String path);

  /**
   * Retrieves the data from the specified path.
   * 
   * @return The byte array representing the file/data.
   */
  byte[] get(String path);
}
