package edu.ntnu.idi.idatt.boardgame;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application extends javafx.application.Application {

    public LadderGame game = new LadderGame();
    public List<Player> players = new ArrayList<>();
    public int currentPlayerIndex = 0;

    @Override
    public void start(Stage stage) throws IOException {

        this.players.add(new Player(1, "Aleks"));
        this.players.add(new Player(2, "Yazan"));

        this.players.forEach(p -> p.placeOnTile(game.getBoard().getTile(0)));

        System.out.printf("Current player: %s\n", players.get(currentPlayerIndex).getName());

        var btn = new Button("Hello!");
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            var dice = Utils.throwDice(1).getFirst();
            Player currentPlayer = players.get(currentPlayerIndex);

            System.out.printf("Player %s rolled %d\n", currentPlayer.getName(), dice);
            System.out.printf("Player moved %d tiles\n", dice);
            currentPlayer.move(dice);
            if (currentPlayer.getCurrentTile().getTileId() == 90) {
                System.out.printf("Player %s won the game!\n", currentPlayer.getName());
                System.exit(0);
            }

            System.out.println("Player status:");
            players.forEach(player -> {
                System.out.printf("  Player(%d, %s) is on tile %d\n", player.getPlayerId(), player.getName(), player.getCurrentTile().getTileId());
            });
            System.out.println("====================================");
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            System.out.printf("\n Current player: %s\n", players.get(currentPlayerIndex).getName());
        });
        var box = new VBox(btn);
        box.setAlignment(javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(box, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}