package edu.ntnu.idi.idatt.boardgame.game.ladder;

import edu.ntnu.idi.idatt.boardgame.actions.LadderAction;
import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Tile;

public class LadderBoard extends Board {

    public LadderBoard() {
        super();

        for (int i = 0; i < 91; i++) {
            addTile(new Tile(i));
            if (i > 0) {
                getTile(i-1).setNextTile(getTile(i));
            }
        }

        // Assign the actions to the tiles
        // ref tiles: https://www.norli.no/media/catalog/product/7/0/7031650150336_2.jpg?auto=webp&format=pjpg&width=1920&height=2400&fit=cover
        getTile(1).setAction(new LadderAction(getTile(40)));
        getTile(8).setAction(new LadderAction(getTile(10)));
        getTile(24).setAction(new LadderAction(getTile(5)));
        getTile(33).setAction(new LadderAction(getTile(3)));
        getTile(36).setAction(new LadderAction(getTile(52)));
        getTile(42).setAction(new LadderAction(getTile(30)));
        getTile(43).setAction(new LadderAction(getTile(62)));
        getTile(49).setAction(new LadderAction(getTile(79)));
        getTile(56).setAction(new LadderAction(getTile(37)));
        getTile(64).setAction(new LadderAction(getTile(27)));
        getTile(65).setAction(new LadderAction(getTile(82)));
        getTile(68).setAction(new LadderAction(getTile(85)));
        getTile(74).setAction(new LadderAction(getTile(12)));
        getTile(87).setAction(new LadderAction(getTile(75)));
    }

}
