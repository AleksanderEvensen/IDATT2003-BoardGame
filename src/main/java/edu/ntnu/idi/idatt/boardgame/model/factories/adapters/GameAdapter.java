package edu.ntnu.idi.idatt.boardgame.model.factories.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import edu.ntnu.idi.idatt.boardgame.model.entities.Board;
import edu.ntnu.idi.idatt.boardgame.model.entities.Game;
import java.lang.reflect.Type;

/**
 * Custom GSON adapter for serializing/deserializing a Game object.
 * <p>
 * This adapter handles the conversion between Game objects and their JSON representation.
 * </p>
 *
 * @see Game
 * @since v1.0.0
 */
public class GameAdapter implements JsonSerializer<Game>, JsonDeserializer<Game> {

  /**
   * Serializes a Game object to its JSON representation.
   *
   * @param src       the Game object to serialize
   * @param typeOfSrc the type of the source object
   * @param context   the context for serialization
   * @return the JSON representation of the Game object
   */
  @Override
  public JsonElement serialize(Game src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject obj = new JsonObject();
    obj.addProperty("id", src.getId());
    obj.addProperty("name", src.getName());
    obj.addProperty("description", src.getDescription());
    obj.add("board", context.serialize(src.getBoard()));
    obj.addProperty("minPlayers", src.getMinPlayers());
    obj.addProperty("maxPlayers", src.getMaxPlayers());
    obj.addProperty("numberOfDice", src.getNumberOfDice());

    return obj;
  }

  /**
   * Deserializes a JSON representation to a Game object.
   *
   * @param json    the JSON element to deserialize
   * @param typeOfT the type of the target object
   * @param context the context for deserialization
   * @return the deserialized Game object
   * @throws JsonParseException if the JSON is not in the expected format
   */
  @Override
  public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObj = json.getAsJsonObject();

    String name = jsonObj.get("name").getAsString();
    String description = jsonObj.get("description").getAsString();
    String id = jsonObj.get("id").getAsString();
    Board board = context.deserialize(jsonObj.get("board"), Board.class);
    int minPlayers = jsonObj.get("minPlayers").getAsInt();
    int maxPlayers = jsonObj.get("maxPlayers").getAsInt();
    int numberOfDice = jsonObj.get("numberOfDice").getAsInt();
    if (jsonObj.get("imagePath") == null) {
      return new Game(board, name, description, id, minPlayers, maxPlayers, numberOfDice, null);
    }
    String imagePath = jsonObj.get("imagePath").getAsString();
    return new Game(board, name, description, id, minPlayers, maxPlayers, numberOfDice, imagePath);
  }
}