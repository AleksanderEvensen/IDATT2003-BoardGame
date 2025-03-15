package edu.ntnu.idi.idatt.boardgame;

import edu.ntnu.idi.idatt.boardgame.actions.LadderAction;
import edu.ntnu.idi.idatt.boardgame.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.game.GameFactory;
import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
        var json = new LocalFileProvider().get("games/ladder.json");
        var gameJson = Utils.bytesToString(json.readAllBytes());
        Game game = GameFactory.createGame(gameJson);

        GridPane boardView = new GridPane();
        AtomicReference<TileComponent> tileView = new AtomicReference<>();
        game.getBoard().getTiles().forEach((k,v) -> {
            tileView.set(new TileComponent(v.getTileId()));
            if(v.getAction().isPresent()){
            }
            tileView.get().setBackgroundColor(Color.YELLOW);
            boardView.add(tileView.get(), v.getCol(), v.getRow());
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