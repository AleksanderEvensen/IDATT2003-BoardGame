package edu.ntnu.idi.idatt.boardgame.game.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import java.io.IOException;

public class TileAdapter extends TypeAdapter<Tile> {

  private final Board board;
  private final Gson gson;

  public TileAdapter(Board board) {
    this.board = board;
    this.gson = new GsonBuilder()
        .registerTypeAdapterFactory(TileActionAdapter.getFactory())
        .registerTypeAdapter(TileAction.class, new LadderActionAdapter(board))
        .create();
  }

  @Override
  public void write(JsonWriter out, Tile tile) throws IOException {
    out.beginObject();
    out.name("tileId").value(tile.getTileId());
    out.name("row").value(tile.getRow());
    out.name("col").value(tile.getCol());
    tile.getNextTile().ifPresent(nextTile -> {
      try {
        out.name("nextTileId").value(nextTile.getTileId());
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    tile.getLastTile().ifPresent(lastTile -> {
      try {
        out.name("lastTileId").value(lastTile.getTileId());
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    tile.getAction().ifPresent(action -> {
      try {
        // Write action as an object directly
        out.name("action");
        out.jsonValue(gson.toJson(action, TileAction.class));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    out.endObject();
  }

  @Override
  public Tile read(JsonReader in) throws IOException {
    in.beginObject();
    int tileId = -1;
    int row = -1;
    int col = -1;
    Integer nextTileId = null;
    Integer lastTileId = null;
    TileAction action = null;

    while (in.hasNext()) {
      switch (in.nextName()) {
        case "tileId":
          tileId = in.nextInt();
          break;
        case "row":
          row = in.nextInt();
          break;
        case "col":
          col = in.nextInt();
          break;
        case "nextTileId":
          nextTileId = in.nextInt();
          break;
        case "lastTileId":
          lastTileId = in.nextInt();
          break;
        case "action":
          action = gson.fromJson(in, TileAction.class);
          break;
        default:
          in.skipValue();
          break;
      }
    }
    in.endObject();

    Tile tile = new Tile(tileId, row, col);

    if (nextTileId != null) {
      Tile nextTile = board.getTile(nextTileId);
      tile.setNextTile(nextTile);
    }

    if (lastTileId != null) {
      Tile lastTile = board.getTile(lastTileId);
      tile.setLastTile(lastTile);
    }

    if (action != null) {
      tile.setAction(action);
    }

    return tile;
  }
}