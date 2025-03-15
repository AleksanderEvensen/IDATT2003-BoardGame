package edu.ntnu.idi.idatt.boardgame.game.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import edu.ntnu.idi.idatt.boardgame.actions.LadderAction;
import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Tile;

import java.io.IOException;

public class LadderActionAdapter extends TypeAdapter<LadderAction> {

  private final Board board;

  public LadderActionAdapter(Board board) {
    this.board = board;
  }

  @Override
  public void write(JsonWriter out, LadderAction ladderAction) throws IOException {
    out.beginObject();
    out.name("type").value("LADDER");
    out.name("destinationTileId").value(ladderAction.getDestinationTile().getTileId());
    out.endObject();
  }

  @Override
  public LadderAction read(JsonReader in) throws IOException {
    in.beginObject();
    int destinationTileId = -1;
    while (in.hasNext()) {
      String name = in.nextName();
      if (name.equals("destinationTileId")) {
        destinationTileId = in.nextInt();
      } else {
        in.skipValue();
      }
    }
    in.endObject();
    Tile destinationTile = board.getTile(destinationTileId);
    if (destinationTile == null) {
      throw new IOException("Destination tile with id " + destinationTileId + " not found");
    }
    return new LadderAction(destinationTile);
  }
}