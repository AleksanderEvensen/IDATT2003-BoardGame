package edu.ntnu.idi.idatt.boardgame.core.filesystem;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalFileProviderTest {

  private LocalFileProvider fileProvider;
  private final String testFilePath = "testDir/testFile.txt";
  private final String testDirPath = "testDir";

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
    InputStream data = new ByteArrayInputStream(content.getBytes());

    // Act
    fileProvider.save(testFilePath, data);

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
    InputStream result = fileProvider.get(testFilePath);

    // Assert
    assertNotNull(result);
    try {
      String retrievedContent = new String(result.readAllBytes());
      assertEquals(content, retrievedContent);
    } catch (IOException e) {
      fail("Failed to read retrieved file content");
    }
  }
}