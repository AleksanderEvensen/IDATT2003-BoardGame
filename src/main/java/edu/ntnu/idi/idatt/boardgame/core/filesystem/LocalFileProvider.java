package edu.ntnu.idi.idatt.boardgame.core.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A FileProvider implementation for storing and retrieving files
 * on the local disk using the default filesystem.
 */
public class LocalFileProvider implements FileProvider {
  Logger logger = Logger.getLogger(LocalFileProvider.class.getName());

  @Override
  public void save(String path, InputStream data) {
    try {
      Path destination = Path.of(path);
      Files.createDirectories(destination.getParent());
      Files.copy(data, destination, StandardCopyOption.REPLACE_EXISTING);

    } catch (IOException e) {
      throw new FileSaveException("Failed to save file at path: " + path);
    }
  }

  @Override
  public boolean delete(String path) {
    try {
      return Files.deleteIfExists(Path.of(path));
    } catch (IOException e) {
      throw new FileDeleteException("Failed to delete file at path: " + path);
    }
  }

  @Override
  public boolean exists(String path) {
    return Files.exists(Path.of(path));
  }

  @Override
  public InputStream get(String path) {
    InputStream stream = getClass().getResourceAsStream("/" + path);
    if (stream != null) {
      return stream;
    }
    if (!exists(path)) {
      logger.info("File does not exist");
      return null;
    }
    try {
      return Files.newInputStream(Path.of(path));
    } catch (IOException e) {
      throw new FileReadException("Failed to read file at path: " + path);
    }
  }
}
