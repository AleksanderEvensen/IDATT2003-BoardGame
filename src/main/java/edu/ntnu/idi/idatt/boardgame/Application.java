package edu.ntnu.idi.idatt.boardgame;

import java.io.IOException;
import java.util.logging.Logger;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.game.GameManager;
import edu.ntnu.idi.idatt.boardgame.game.PlayerManager;
import edu.ntnu.idi.idatt.boardgame.router.Router;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.IView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.view.GameView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.view.MainMenuView;
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

    private static Scene primaryScene;
    private static Stage primaryStage;

    public int currentPlayerIndex = 0;

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
     *        set.
     * @throws IOException if an input or output exception occurs.
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryScene = new Scene(new Pane(), 1200, 1000);

        router.createRoute("/home", new MainMenuView());
        router.createRoute("/game/:gameId", new GameView());

        router.navigate("/game/ladder");

        stage.setFullScreen(false);
        stage.setTitle("Board Game");
        stage.setScene(primaryScene);
        stage.show();
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

    public static void closeApplication() {
        if (primaryStage != null) {
            primaryStage.close();
        }
    }

    public static Scene getScene() {
        return primaryScene;
    }
}
