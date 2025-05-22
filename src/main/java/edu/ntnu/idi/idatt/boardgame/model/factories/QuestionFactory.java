package edu.ntnu.idi.idatt.boardgame.model.factories;

import com.google.gson.JsonObject;
import edu.ntnu.idi.idatt.boardgame.model.entities.Question;
import edu.ntnu.idi.idatt.boardgame.model.entities.QuestionCategory;
import java.util.HashMap;

/**
 * Factory class for creating Question objects.
 */
public class QuestionFactory {

  /**
   * Creates a Question object from a JSON object.
   *
   * @param jsonObject The JSON object containing question data.
   * @return A Question object.
   */
  public static Question createQuestion(JsonObject jsonObject) {
    String questionText = jsonObject.get("question").getAsString();
    QuestionCategory category = QuestionCategory.valueOf(jsonObject.get("category").getAsString());

    JsonObject answersJson = jsonObject.getAsJsonObject("answers");
    HashMap<Integer, String> answers = new HashMap<>();
    answersJson.entrySet().forEach(entry ->
        answers.put(Integer.parseInt(entry.getKey()), entry.getValue().getAsString())
    );

    int correctAnswerIndex = jsonObject.get("correctAnswerIndex").getAsInt();

    return new Question(questionText, category, answers, correctAnswerIndex);
  }
}