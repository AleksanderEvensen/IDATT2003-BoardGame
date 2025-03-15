package edu.ntnu.idi.idatt.boardgame.model;

public class Game {

    private final Board board;
    private final String name;
    private final String description;
    private final String id;

    public Game(Board board, String name, String description, String id) {
        this.board = board;
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getId() {
        return id;
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
