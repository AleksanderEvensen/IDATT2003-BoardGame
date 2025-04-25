package edu.ntnu.idi.idatt.boardgame.game;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.player.JavaFXPlayer;
import javafx.scene.paint.Color;

/**
 * Manages players for the game, including loading and saving player data.
 */
public class PlayerManager {
    private final List<Player> players = new ArrayList<>();
    private final LocalFileProvider fileProvider;
    private final Logger logger = Logger.getLogger(PlayerManager.class.getName());

    /**
     * Constructs a PlayerManager with the specified file provider.
     *
     * @param fileProvider the file provider for loading and saving player data
     */
    public PlayerManager(LocalFileProvider fileProvider) {
        this.fileProvider = fileProvider;
        loadJFXPlayers("players.csv");
    }

    /**
     * Loads JavaFX players from a file.
     *
     * @param path the path to the player data file
     */
    public void loadJFXPlayers(String path) {
        try {
            InputStream stream = fileProvider.get(path);

            var playersData = new String(stream.readAllBytes());

            players.clear();
            playersData.lines().forEach(line -> {
                String[] data = line.split(";");
                if (data.length != 3) {
                    logger.warning(String.format("Invalid player data: '%s'", line));
                    return;
                }

                String name = data[0];
                Color color = Color.web(data[1]);
                String icon = data[2];

                players.add(new JavaFXPlayer(players.size() + 1, name, color, icon));
            });

            logger.info(String.format("Loaded %d players from '%s'", players.size(), path));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load players from path: " + path, e);
            // Create default players if unable to load
            createDefaultPlayers();
        }
    }

    /**
     * Creates default players if no players are loaded.
     */
    private void createDefaultPlayers() {
        if (players.isEmpty()) {
            players.add(new JavaFXPlayer(1, "Player 1", Color.LIGHTBLUE, "djinn.png"));
            players.add(new JavaFXPlayer(2, "Player 2", Color.LIGHTCORAL, "drakkar-dragon.png"));
            players.add(new JavaFXPlayer(3, "Player 3", Color.LIGHTGREEN, "eight-ball.png"));
            players.add(new JavaFXPlayer(4, "Player 4", Color.LIGHTYELLOW, "horned-reptile.png"));
            logger.info("Created default players");
        }
    }

    /**
     * Saves players to a file.
     *
     * @param path the path to save to
     * @param players the players to save
     */
    public void savePlayers(String path, List<Player> players) {
        StringBuilder sb = new StringBuilder();
        for (Player player : players) {
            if (player instanceof JavaFXPlayer) {
                JavaFXPlayer jfxPlayer = (JavaFXPlayer) player;
                sb.append(String.format("%s;%s;%s\n", jfxPlayer.getName(),
                        formatColor(jfxPlayer.getColor()), jfxPlayer.getPawnImageName()));
            } else {
                logger.warning("Player is not a JavaFXPlayer: " + player);
            }
        }

        try {
            fileProvider.save(path, new ByteArrayInputStream(sb.toString().getBytes()));
            // Update the internal players list
            this.players.clear();
            this.players.addAll(players);
            logger.info(String.format("Saved %d players to '%s'", players.size(), path));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save players to path: " + path, e);
        }
    }

    /**
     * Formats a color as a CSS color string.
     */
    private String formatColor(Color color) {
        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }

    /**
     * Gets the current list of players.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Adds a new player.
     *
     * @param player the player to add
     * @return true if the player was added, false otherwise
     */
    public boolean addPlayer(Player player) {
        if (player == null) {
            logger.warning("Attempted to add null player");
            return false;
        }

        // Assign a unique player ID
        if (player.getPlayerId() <= 0) {
            int maxId = 0;
            for (Player p : players) {
                maxId = Math.max(maxId, p.getPlayerId());
            }
            if (player instanceof JavaFXPlayer) {
                JavaFXPlayer jfxPlayer = (JavaFXPlayer) player;
                players.add(new JavaFXPlayer(maxId + 1, jfxPlayer.getName(), jfxPlayer.getColor(),
                        jfxPlayer.getPawnImageName()));
            } else {
                players.add(new JavaFXPlayer(maxId + 1, player.getName()));
            }
        } else {
            players.add(player);
        }

        logger.info("Added player: " + player.getName());
        return true;
    }

    /**
     * Removes a player by ID.
     *
     * @param playerId the ID of the player to remove
     * @return true if the player was removed, false if not found
     */
    public boolean removePlayer(int playerId) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerId() == playerId) {
                Player removed = players.remove(i);
                logger.info("Removed player: " + removed.getName());
                return true;
            }
        }

        logger.warning("Player with ID " + playerId + " not found for removal");
        return false;
    }

    /**
     * Removes a specific player.
     *
     * @param player the player to remove
     * @return true if the player was removed, false if not found
     */
    public boolean removePlayer(Player player) {
        if (player == null) {
            logger.warning("Attempted to remove null player");
            return false;
        }

        boolean result = players.remove(player);
        if (result) {
            logger.info("Removed player: " + player.getName());
        } else {
            logger.warning("Player not found for removal: " + player.getName());
        }

        return result;
    }

    /**
     * Updates player information.
     *
     * @param playerId the ID of the player to update
     * @param updatedPlayer the player with updated information
     * @return true if the player was updated, false if not found
     */
    public boolean updatePlayer(int playerId, Player updatedPlayer) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerId() == playerId) {
                players.set(i, updatedPlayer);
                logger.info("Updated player information for: " + updatedPlayer.getName());
                return true;
            }
        }

        logger.warning("Player with ID " + playerId + " not found for update");
        return false;
    }
}
