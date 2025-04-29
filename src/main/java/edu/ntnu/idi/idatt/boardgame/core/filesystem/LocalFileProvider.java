package edu.ntnu.idi.idatt.boardgame.core.filesystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Logger;

/**
 * A FileProvider implementation for storing and retrieving files on the local disk using the
 * default filesystem.
 * <p>
 * This class provides methods to save, delete, check existence, and retrieve files.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.core.filesystem.FileProvider
 * @since v1.0.0
 */
public class LocalFileProvider implements FileProvider {
  Logger logger = Logger.getLogger(LocalFileProvider.class.getName());

  /**
   * Saves the given data to the specified path.
   * <p>
   * Creates directories if they do not exist and replaces existing files.
   * </p>
   *
   * @param path The file path or identifier where data should be saved.
   * @param bytes The byte array representing the file/data to be saved.
   * @throws FileSaveException if an error occurs while saving the file.
   * @see edu.ntnu.idi.idatt.boardgame.core.filesystem.FileSaveException
   */
  @Override
  public void save(String path, byte[] bytes) {
    Path filePath = Paths.get(path);
    if (Files.notExists(filePath.getParent())) {
      try {
        Files.createDirectories(filePath.getParent());
      } catch (IOException e) {
        logger.warning("Failed to create directories for path: " + path);
        throw new FileSaveException("Failed to create directories for path: " + path);
      }
    }
    try (OutputStream writer = Files.newOutputStream(filePath, StandardOpenOption.CREATE)) {
      writer.write(bytes);
    } catch (IOException e) {
      e.printStackTrace();
      logger.warning("Failed to save file at path: " + path);
      throw new FileSaveException("Failed to save file at path: " + path);
    }
  }

  /**
   * Deletes the file or data at the specified path.
   * <p>
   * Returns true if the file was deleted, false if no file existed at that path.
   * </p>
   *
   * @param path The file path or identifier to delete.
   * @return true if the file was deleted, false if no file existed at that path.
   * @throws FileDeleteException if an error occurs while deleting the file.
   * @see edu.ntnu.idi.idatt.boardgame.core.filesystem.FileDeleteException
   */
  @Override
  public boolean delete(String path) {
    try {
      return Files.deleteIfExists(Path.of(path));
    } catch (IOException e) {
      e.printStackTrace();
      throw new FileDeleteException("Failed to delete file at path: " + path);
    }
  }

  /**
   * Checks if a file or data exists at the specified path.
   *
   * @param path The file path or identifier.
   * @return true if the file exists, false otherwise.
   */
  @Override
  public boolean exists(String path) {
    return Files.exists(Path.of(path));
  }

  /**
   * Retrieves the file/data as an {@link InputStream}, or returns null if not found.
   * <p>
   * First checks the classpath, then the filesystem.
   * </p>
   *
   * @param path The file path or identifier to retrieve.
   * @return An InputStream of the requested file/data, or null if it does not exist.
   * @throws FileReadException if an error occurs while loading the file.
   * @see FileReadException
   */
  @Override
  public byte[] get(String path) {
    File file = new File(path);
    try (BufferedInputStream buff = new BufferedInputStream(new FileInputStream(file))) {
      return buff.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
      logger.warning("Failed to read file from path: " + path);
      throw new FileReadException("Failed to read file at path: " + path);
    }
  }

  /**
   * Lists all files in the specified directory.
   *
   * @param path The directory path to list files from.
   * @return An array of file names in the specified directory.
   */
  @Override
  public List<String> listFiles(String path) {
    File directory = new File(path);
    if (!directory.isDirectory()) {
      throw new IllegalArgumentException("Path is not a directory: " + path);
    }
    return List.of(directory.list());
  }
}
