package edu.ntnu.idi.idatt.boardgame.model;

public class Game {

    private final Board board;
    private final String name;
    private final String description;

    public Game(Board board, String name, String description) {
        this.board = board;
        this.name = name;
        this.description = description;
    }

    public Board getBoard() {
        return board;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
