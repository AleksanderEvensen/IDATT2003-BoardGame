package edu.ntnu.idi.idatt.boardgame.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.FileProvider;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.model.quiz.Question;
import edu.ntnu.idi.idatt.boardgame.model.quiz.QuestionCategory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages quiz questions for the game, including loading and processing question data.
 * <p>
 * This class is responsible for loading quiz questions from a file and providing access to the
 * loaded questions.
 * </p>
 *
 * @version 1.0.0
 * @since v1.0.0
 */
public class QuizManager {

  private final FileProvider fileProvider;
  Set<Question> questions;

  /**
   * Constructs a QuizManager with the specified file provider.
   *
   * @param fileProvider the file provider for loading question files
   */
  public QuizManager(FileProvider fileProvider) {
    this.fileProvider = fileProvider;
    this.questions = new HashSet<>();
    loadQuestions("data/questions.json");
  }


  /**
   * Retrieves a random question from the loaded questions.
   * <p>
   * If no questions are available, null is returned.
   * </p>
   *
   * @return a random question, or null if none are available
   */
  public Question getRandomQuestion() {
    if (questions.isEmpty()) {
      return null;
    }
    Integer questionIndex = Utils.getRandomNumber(0, questions.size());
    return questions.stream().skip(questionIndex).findFirst().orElse(null);
  }

  /**
   * Retrieves a random question from the specified category.
   * <p>
   * If no questions are available in the specified category, null is returned.
   * </p>
   *
   * @param category the category of the question
   * @return a random question from the specified category, or null if none are available
   */
  public Question getRandomQuestionFromCategory(QuestionCategory category) {
    if (questions.isEmpty()) {
      return null;
    }
    List<Question> filteredQuestions =
        questions.stream().filter(question -> question.getCategory() == category).toList();
    if (filteredQuestions.isEmpty()) {
      return null;
    }
    Integer questionIndex = Utils.getRandomNumber(0, filteredQuestions.size());
    return filteredQuestions.stream().skip(questionIndex).findFirst().orElse(null);
  }

  /**
   * Loads quiz questions from the specified file path.
   * <p>
   * The questions are loaded from a JSON file and stored in the questions set.
   * </p>
   *
   * @param path the path to the question file
   */
  private void loadQuestions(String path) {
    try {
      String jsonData = new String(fileProvider.get(path));
      JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();
      for (JsonElement element : jsonArray) {
        if (element.isJsonNull()) {
          continue;
        }
        if (!element.isJsonObject()) {
          continue;
        }
        questions.add(QuestionFactory.createQuestion(element.getAsJsonObject()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static QuizManager instance;

  /**
   * Creates and/or gets the singleton instance of {@link QuizManager}.
   *
   * @return the singleton instance of {@link QuizManager}
   */
  public static QuizManager getInstance() {
    if (instance == null) {
      instance = new QuizManager(new LocalFileProvider());
    }
    return instance;
  }
}
