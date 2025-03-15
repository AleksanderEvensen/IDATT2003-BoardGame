package edu.ntnu.idi.idatt.boardgame;

import edu.ntnu.idi.idatt.boardgame.actions.HasStyleResolver;
import edu.ntnu.idi.idatt.boardgame.actions.TileActionStyleResolver;
import edu.ntnu.idi.idatt.boardgame.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.game.GameManager;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

/**
 * The main application class.
 */
public class Application extends javafx.application.Application {
    public List<Player> players = new ArrayList<>();
    public int currentPlayerIndex = 0;

    /**
     * The main entry point for all JavaFX applications.
     * @param stage the primary stage for this application, onto which the application scene can be set.
     * @throws IOException if an input or output exception occurs.
     */
    @Override
    public void start(Stage stage) throws IOException {
        GameManager gameManager = new GameManager(new LocalFileProvider());

        gameManager.getGame("ladder").getBoard().getTiles().entrySet().forEach(entry -> {
            Tile tile = entry.getValue();
            tile.getAction().ifPresent(action -> {
                System.out.println(action);
            });
        });

        GridPane boardView = new GridPane();
        AtomicReference<TileComponent> tileView = new AtomicReference<>();
        gameManager.getGame("ladder").getBoard().getTiles().entrySet().forEach(entry -> {
            Tile tile = entry.getValue();
            TileComponent tileComponent = new TileComponent(tile.getTileId());
            boardView.add(tileComponent, tile.getCol(), tile.getRow());
            tileView.set(tileComponent);
        });

        gameManager.getGame("ladder").getBoard().getTiles().entrySet().forEach(entry -> {
            Tile tile = entry.getValue();
            tile.getAction().ifPresent(action -> {
                if (action instanceof HasStyleResolver) {
                    TileActionStyleResolver styleResolver = ((HasStyleResolver) action).getStyleResolver();
                    styleResolver.resolveStyle(tile, action, boardView);
                }
            });
        });



        Scene scene = new Scene(boardView, 800, 800);
        scene.getStylesheets().add("main.css");
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}