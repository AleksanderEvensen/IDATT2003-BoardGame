package edu.ntnu.idi.idatt.boardgame.model.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.FileProvider;
import edu.ntnu.idi.idatt.boardgame.model.entities.Question;
import edu.ntnu.idi.idatt.boardgame.model.entities.QuestionCategory;
import edu.ntnu.idi.idatt.boardgame.model.factories.QuestionFactory;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * The QuizManager class is responsible for managing quiz questions and their categories. It
 * provides functionality to load questions from a file, retrieve random questions, and retrieve
 * random questions from specific categories.
 */
public class QuizManager {

  private static QuizManager instance;
  private static volatile Supplier<FileProvider> fileProviderSupplier;
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
  private QuizManager(FileProvider fileProvider) {
    this.fileProvider = fileProvider;
  }

  /**
   * Constructs a QuizManager with a specified file provider.
   *
   * @param fileProvider the file provider to use for loading game files
   */
  public static synchronized void init(Supplier<FileProvider> fileProvider) {
    if (instance != null) {
      throw new IllegalStateException("QuizManager already initialised");
    }
    if (fileProvider == null) {
      throw new NullPointerException("fileProvider must not be null");
    }
    fileProviderSupplier = fileProvider;
  }

  /**
   * Retrieves the singleton instance of QuizManager. If the instance does not exist, it is created
   * with a LocalFileProvider.
   *
   * @return The singleton instance of QuizManager.
   */
  public static synchronized QuizManager getInstance() {
    if (fileProviderSupplier == null) {
      throw new IllegalStateException("QuizManager not initialised");
    }
    if (instance == null) {
      return instance = new QuizManager(fileProviderSupplier.get());
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
    int idx = Utils.getRandomNumber(0, questions.size() - 1);
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
  public void loadQuestions(String path) {
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