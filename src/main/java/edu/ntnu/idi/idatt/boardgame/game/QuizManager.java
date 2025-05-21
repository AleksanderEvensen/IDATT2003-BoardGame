package edu.ntnu.idi.idatt.boardgame.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.FileProvider;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.model.quiz.Question;
import edu.ntnu.idi.idatt.boardgame.model.quiz.QuestionCategory;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The QuizManager class is responsible for managing quiz questions and their categories. It
 * provides functionality to load questions from a file, retrieve random questions, and retrieve
 * random questions from specific categories.
 */
public class QuizManager {

  private static QuizManager instance;
  private final FileProvider fileProvider;
  private final Logger logger = Logger.getLogger(QuizManager.class.getName());
  private final List<Question> questions = new ArrayList<>();
  private final Map<QuestionCategory, List<Question>> questionsByCategory =
      new EnumMap<>(QuestionCategory.class);

  /**
   * Constructs a QuizManager instance with the specified FileProvider. Loads questions from the
   * default file path.
   *
   * @param fileProvider The FileProvider used to access the question file.
   */
  public QuizManager(FileProvider fileProvider) {
    this.fileProvider = fileProvider;
    loadQuestions("data/questions.json");
  }

  /**
   * Retrieves the singleton instance of QuizManager. If the instance does not exist, it is created
   * with a LocalFileProvider.
   *
   * @return The singleton instance of QuizManager.
   */
  public static QuizManager getInstance() {
    if (instance == null) {
      instance = new QuizManager(new LocalFileProvider());
    }
    return instance;
  }

  /**
   * Retrieves a random question from the list of loaded questions.
   *
   * @return A random Question, or null if no questions are available.
   */
  public Question getRandomQuestion() {
    if (questions.isEmpty()) {
      return null;
    }
    int idx = Utils.getRandomNumber(0, questions.size());
    return questions.get(idx);
  }

  /**
   * Retrieves a random question from a specific category.
   *
   * @param category The category from which to retrieve a random question.
   * @return A random Question from the specified category, or null if no questions are available in
   * that category.
   */
  public Question getRandomQuestionFromCategory(QuestionCategory category) {
    List<Question> list = questionsByCategory.get(category);
    if (list == null || list.isEmpty()) {
      return null;
    }
    int idx = Utils.getRandomNumber(0, list.size() - 1);
    return list.get(idx);
  }

  /**
   * Loads questions from the specified file path. Parses the JSON data and populates the questions
   * and questionsByCategory collections.
   *
   * @param path The file path to load questions from.
   */
  private void loadQuestions(String path) {
    try {
      String jsonData = new String(fileProvider.get(path), StandardCharsets.UTF_8);
      JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();

      for (JsonElement element : jsonArray) {
        if (!element.isJsonObject()) {
          continue;
        }
        Question q = QuestionFactory.createQuestion(element.getAsJsonObject());
        questions.add(q);
        questionsByCategory.computeIfAbsent(q.getCategory(), __ -> new ArrayList<>()).add(q);
      }
    } catch (Exception e) {
      logger.warning("Failed to load questions from path: " + path);
      logger.warning(e.getMessage());
    } finally {
      logger.info(String.format("Loaded %d questions from '%s'", questions.size(), path));
    }
  }
}