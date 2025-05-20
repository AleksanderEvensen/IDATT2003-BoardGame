package edu.ntnu.idi.idatt.boardgame.core.filesystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalFileProviderTest {

  private final String testFilePath = "testDir/testFile.txt";
  private final String testDirPath = "testDir";
  private LocalFileProvider fileProvider;

  @BeforeEach
  void setUp() {
    fileProvider = new LocalFileProvider();
  }

  @AfterEach
  void tearDown() throws IOException {
    Files.deleteIfExists(Path.of(testFilePath));
    Files.deleteIfExists(Path.of(testDirPath));
  }

  @Test
  void save() {
    // Arrange
    String content = "Hello, World!";

    // Act
    fileProvider.save(testFilePath, content.getBytes());

    // Assert
    Path path = Path.of(testFilePath);
    assertTrue(Files.exists(path));
    try {
      String savedContent = Files.readString(path);
      assertEquals(content, savedContent);
    } catch (IOException e) {
      fail("Failed to read saved file content");
    }
  }

  @Test
  void delete() throws IOException {
    // Arrange
    Files.createDirectories(Path.of(testDirPath));
    Path path = Path.of(testFilePath);
    Files.createFile(path);

    // Act
    boolean deleted = fileProvider.delete(testFilePath);

    // Assert
    assertTrue(deleted);
    assertFalse(Files.exists(path));
  }

  @Test
  void exists() throws IOException {
    // Arrange
    Files.createDirectories(Path.of(testDirPath));
    Files.createFile(Path.of(testFilePath));

    // Act & Assert
    assertTrue(fileProvider.exists(testFilePath));
    assertFalse(fileProvider.exists("nonExistentFile.txt"));
  }

  @Test
  void get() throws IOException {
    // Arrange
    String content = "Hello, World!";
    Files.createDirectories(Path.of(testDirPath));
    Files.writeString(Path.of(testFilePath), content);

    // Act
    byte[] result = fileProvider.get(testFilePath);

    // Assert
    assertNotNull(result);

    String retrievedContent = new String(result);
    assertEquals(content, retrievedContent);
  }


  @Test
  void save_nullData_shouldThrowNullPointerException() {
    // Arrange
    String path = testFilePath;

    // Act & Assert
    assertThrows(NullPointerException.class, () -> fileProvider.save(path, null));
  }

  @Test
  void delete_nonExistentFile_shouldReturnFalse() {
    // Act
    boolean result = fileProvider.delete("nonExistentFile.txt");

    // Assert
    assertFalse(result);
  }


  @Test
  void get_invalidPath_shouldThrowFileReadException() {
    // Arrange
    String invalidPath = "invalid:/path/file.txt";

    // Act & Assert
    assertThrows(FileReadException.class, () -> fileProvider.get(invalidPath));
  }

  @Test
  void listFiles_nonExistentDirectory_shouldThrowIllegalArgumentException() {
    // Act & Assert
    assertThrows(DirectoryListException.class, () -> fileProvider.listFiles("nonExistentDir"));
  }

  @Test
  void listFiles_fileInsteadOfDirectory_shouldThrowIllegalArgumentException() throws IOException {

    // Act & Assert
    assertThrows(DirectoryListException.class, () -> fileProvider.listFiles(testFilePath));
  }
}