package edu.ntnu.idi.idatt.boardgame;

import edu.ntnu.idi.idatt.boardgame.core.filesystem.FileProvider;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.core.router.Router;
import edu.ntnu.idi.idatt.boardgame.javafx.IView;
import edu.ntnu.idi.idatt.boardgame.javafx.providers.ToastProvider;
import edu.ntnu.idi.idatt.boardgame.javafx.view.GameLobbyView;
import edu.ntnu.idi.idatt.boardgame.javafx.view.MainMenuView;
import edu.ntnu.idi.idatt.boardgame.model.managers.GameManager;
import edu.ntnu.idi.idatt.boardgame.model.managers.PlayerManager;
import edu.ntnu.idi.idatt.boardgame.model.managers.QuizManager;
import java.io.IOException;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * The main application class.
 */
public class Application extends javafx.application.Application {

  private static final Logger logger = Logger.getLogger(Application.class.getName());

  private static Scene primaryScene;
  @Getter
  private static Stage primaryStage;
  @Getter
  private static boolean isDarkTheme = true;

  private FileProvider fileProvider;

  public static void main(String[] args) {
    launch();
  }

  public static void closeApplication() {
    if (primaryStage != null) {
      primaryStage.close();
    }
  }

  public static void refreshCss() {
    if (primaryScene != null) {
      primaryScene.getStylesheets().clear();
      if (isDarkTheme) {
        primaryScene.getStylesheets().add("theme.css");
      } else {
        primaryScene.getStylesheets().add("theme-light.css");
      }
      primaryScene.getStylesheets().add("main.css");
      logger.info("CSS reloaded");
    }
  }

  /**
   * Sets and refreshes the theme setting.
   *
   * @param isDarkTheme true for dark theme, false for light theme.
   */
  public static void setDarkTheme(boolean isDarkTheme) {
    Application.isDarkTheme = isDarkTheme;
    refreshCss();
  }

  public static Scene getScene() {
    return primaryScene;
  }

  /**
   * The main entry point for all JavaFX applications.
   *
   * @param stage the primary stage for this application, onto which the application scene can be
   *              set.
   * @throws IOException if an input or output exception occurs.
   */
  @Override
  public void start(Stage stage) throws IOException {
    fileProvider = new LocalFileProvider();
    GameManager.init(() -> fileProvider);
    GameManager.getInstance().loadGamesFromDefaultPath();

    QuizManager.init(() -> fileProvider);
    QuizManager.getInstance().loadQuestions("data/questions.json");

    PlayerManager.init(() -> fileProvider);
    PlayerManager.getInstance().loadPlayers("data/players.csv");

    primaryStage = stage;
    StackPane root = new StackPane();
    primaryScene = new Scene(root, 1920, 1080);

    router.createRoute("/home", new MainMenuView());
    router.createRoute("/game/:gameId", new GameLobbyView());

    router.navigate("/home");

    refreshCss();
    primaryScene.getStylesheets().add("main.css");
    stage.setFullScreen(true);
    stage.setTitle("Board Game");
    stage.setScene(primaryScene);
    stage.show();
  }

  /**
   * The router used for navigating between JavaFX views
   */
  public static final Router<IView> router = new Router<>(ctx -> {
    IView view = ctx.getData();
    view.load(ctx);
    var lastCtx = Application.router.getCurrentContext();
    if (lastCtx != null) {
      IView lastView = lastCtx.getData();
      lastView.unload();
    }
    StackPane viewRoot = view.createRoot();
    ToastProvider.setRoot(viewRoot);
    Application.primaryScene.setRoot(viewRoot);
  });


}
