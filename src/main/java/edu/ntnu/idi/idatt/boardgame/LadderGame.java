package edu.ntnu.idi.idatt.boardgame;

import edu.ntnu.idi.idatt.boardgame.game.ladder.LadderBoard;

public class LadderGame {

    private LadderBoard board;

    public LadderGame() {
        this.board = new LadderBoard();
    }

    public LadderBoard getBoard() {
        return board;
    }
}
