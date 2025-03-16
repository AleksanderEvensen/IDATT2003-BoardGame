package edu.ntnu.idi.idatt.boardgame.game.adapters;


import com.google.gson.*;
import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Game;

import java.lang.reflect.Type;

/**
 * Custom GSON adapter for serializing/deserializing a Game object.
 */
public class GameAdapter implements JsonSerializer<Game>, JsonDeserializer<Game> {

  @Override
  public JsonElement serialize(Game src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject obj = new JsonObject();
    obj.addProperty("id", src.getId());
    obj.addProperty("name", src.getName());
    obj.addProperty("description", src.getDescription());
    obj.add("board", context.serialize(src.getBoard()));

    return obj;
  }

  @Override
  public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObj = json.getAsJsonObject();

    String name = jsonObj.get("name").getAsString();
    String description = jsonObj.get("description").getAsString();
    String id = jsonObj.get("id").getAsString();
    Board board = context.deserialize(jsonObj.get("board"), Board.class);
    return new Game(board, name, description, id);
  }
}
