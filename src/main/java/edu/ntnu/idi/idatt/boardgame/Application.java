package edu.ntnu.idi.idatt.boardgame;

import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.game.GameManager;
import edu.ntnu.idi.idatt.boardgame.game.PlayerManager;
import edu.ntnu.idi.idatt.boardgame.game.QuizManager;
import edu.ntnu.idi.idatt.boardgame.router.Router;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.IView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.audio.AudioManager;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.view.GameLobbyView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.view.MainMenu.MainMenuController;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.view.MainMenu.MainMenuView;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The main application class.
 */
public class Application extends javafx.application.Application {

  private static final Logger logger = Logger.getLogger(Application.class.getName());
  private static final LocalFileProvider fileProvider = new LocalFileProvider();
  private static final GameManager gameManager = new GameManager(fileProvider);
  private static final PlayerManager playerManager = new PlayerManager(fileProvider);
  private static final QuizManager quizManager = new QuizManager(fileProvider);
  private static final AudioManager audioManager = new AudioManager(fileProvider);

  private static Scene primaryScene;
  private static Stage primaryStage;


  /**
   * The router used for navigating between JavaFX views
   */
  public static final Router<IView> router = new Router<>(ctx -> {
    try {
      var lastCtx = Application.router.getCurrentContext();
      if (lastCtx != null) {
        IView lastView = lastCtx.getData();
        lastView.unload();
      }
    } catch (Exception e) {
      logger.info("Failed to unloading last view");
      e.printStackTrace();
    }

    try {
      IView view = ctx.getData();
      view.load(ctx);
      Parent viewRoot = view.createRoot();
      Application.primaryScene.setRoot(viewRoot);
    } catch (Exception e) {
      logger.info("Failed to navigate to view: " + ctx.getUrl());
      e.printStackTrace();
    }
  });

  /**
   * The main entry point for all JavaFX applications.
   *
   * @param stage the primary stage for this application, onto which the application scene can be
   *              set.
   * @throws IOException if an input or output exception occurs.
   */
  @Override
  public void start(Stage stage) throws IOException {
    primaryStage = stage;
    primaryScene = new Scene(new Pane(), 1200, 1000);

    router.createRoute("/home", new MainMenuView(new MainMenuController()));
    router.createRoute("/game/:gameId", new GameLobbyView());

    router.navigate("/home");

    startFileWatcher();

    primaryScene.getStylesheets().add("main.css");
    stage.setFullScreen(false);
    stage.setTitle("Board Game");
    stage.setScene(primaryScene);
    stage.show();
  }

  private void startFileWatcher() {
    Thread fileWatcherThread = new Thread(() -> {
      try {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        // Watch the src/resources directory and all subdirectories
        Path resourcesPath = Paths.get("src", "main", "resources");
        if (!Files.exists(resourcesPath)) {
          logger.warning("Resources directory not found: " + resourcesPath);
          return; // Exit if the directory does not exist
        }

        // Register all subdirectories recursively
        registerAll(resourcesPath, watchService);

        boolean running = true;
        while (running) {
          WatchKey key = watchService.take(); // This call blocks
          if (key == null) {
            running = false;
            continue;
          }
          for (WatchEvent<?> event : key.pollEvents()) {

            // Get the name of the file that was modified
            WatchEvent<Path> ev = (WatchEvent<Path>) event;
            Path changedFile = ev.context();
            Path dir = (Path) key.watchable();
            Path fullPath = dir.resolve(changedFile);

            // Check if the modified file is a CSS file
            if (fullPath.toString().endsWith(".css")) {
              logger.info("CSS file changed: " + fullPath);
              // Use Platform.runLater to execute the CSS refresh on the JavaFX
              // application thread
              Thread.sleep(500);

              Platform.runLater(() -> refreshCss());
            }
          }
          running = key.reset();
        }
      } catch (IOException | InterruptedException e) {
        logger.severe("File watcher interrupted or failed: " + e.getMessage());
        e.printStackTrace();
      }
    }, "File Watcher");
    fileWatcherThread.setDaemon(true); // Allow the application to exit even if this thread is
    // running
    fileWatcherThread.start();
  }

  private void registerAll(final Path dir, final WatchService watchService) throws IOException {
    // Registers the directory and all subdirectories.
    Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path subDir, BasicFileAttributes attrs)
          throws IOException {
        subDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        return FileVisitResult.CONTINUE;
      }
    });
  }

  public static void main(String[] args) {
    launch();
  }

  public static GameManager getGameManager() {
    return gameManager;
  }

  public static PlayerManager getPlayerManager() {
    return playerManager;
  }

  public static QuizManager getQuizManager() {
    return quizManager;
  }

  public static AudioManager getAudioManager() {
    return audioManager;
  }


  public static void closeApplication() {
    if (primaryStage != null) {
      primaryStage.close();
    }
  }

  public static void refreshCss() {
    if (primaryScene != null) {
      primaryScene.getStylesheets().clear();
      primaryScene.getStylesheets().add("main.css");
      logger.info("CSS reloaded");
    }
  }

  public static Scene getScene() {
    return primaryScene;
  }
}
