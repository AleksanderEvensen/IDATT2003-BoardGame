package edu.ntnu.idi.idatt.boardgame.ui.javafx.view.MainMenu;

import java.util.List;
import java.util.logging.Logger;
import edu.ntnu.idi.idatt.boardgame.Application;
import edu.ntnu.idi.idatt.boardgame.core.reactivity.Observer;
import edu.ntnu.idi.idatt.boardgame.game.PlayerManager;
import edu.ntnu.idi.idatt.boardgame.model.Color;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

public class MainMenuController implements Observer<PlayerManager, List<Player>> {
    private final Logger logger = Logger.getLogger(MainMenuController.class.getName());


    private @Getter ObservableList<Player> players;


    public MainMenuController() {
        Application.getPlayerManager().addListener(this);
        this.players = FXCollections.observableArrayList();
    }

    public void initialize() {
        this.players.setAll(Application.getPlayerManager().getPlayers());
    }

    public void exitToDesktop() {
        Application.closeApplication();
    }

    public void startGame(String gameId) {
        Application.router.navigate(String.format("/game/%s", gameId));
    }

    public void addPlayer(Player player) {
        logger.info("ADDing player: " + player.getName());
        Application.getPlayerManager().addPlayer(player);
    }

    public void removePlayer(Player player) {
        Application.getPlayerManager().removePlayer(player);
    }

    public void updatePlayer(Player player, String newName, Color newColor) {
        Application.getPlayerManager().updatePlayer(player, newName, newColor);
    }

    public void savePlayersToFile() {
        Application.getPlayerManager().savePlayers("data/players.csv",
                Application.getPlayerManager().getPlayers());
    }

    // Observers
    @Override
    public void update(List<Player> value) {
        logger.info("Updating players from observer: " + value);
        this.players.setAll(value);
    }
}
