package edu.ntnu.idi.idatt.boardgame.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.ntnu.idi.idatt.boardgame.core.filesystem.FileProvider;
import edu.ntnu.idi.idatt.boardgame.model.entities.Question;
import edu.ntnu.idi.idatt.boardgame.model.entities.QuestionCategory;
import edu.ntnu.idi.idatt.boardgame.model.managers.QuizManager;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

class QuizManagerTest {

  private static final String SAMPLE_JSON = """
      [
        {
          "question": "What is the capital of France?",
          "category": "GEOGRAPHY",
          "answers": { "0": "Moscow", "1": "Madrid", "2": "Paris", "3": "Brasília" },
          "correctAnswerIndex": 2
        },
        {
          "question": "What is the capital of Germany?",
          "category": "GEOGRAPHY",
          "answers": { "0": "Paris", "1": "Berlin", "2": "Ottawa", "3": "Canberra" },
          "correctAnswerIndex": 1
        },
        {
          "question": "What is the capital of Italy?",
          "category": "GEOGRAPHY",
          "answers": { "0": "Rome", "1": "Ottawa", "2": "New Delhi", "3": "Cairo" },
          "correctAnswerIndex": 0
        }
      ]""";

  private AutoCloseable mocks;
  private FileProvider fileProvider;

  @BeforeEach
  void setup() throws Exception {
    mocks = MockitoAnnotations.openMocks(this);
    fileProvider = mock(FileProvider.class);
    when(fileProvider.get("data/questions.json"))
        .thenReturn(SAMPLE_JSON.getBytes(StandardCharsets.UTF_8));
    resetQuizManagerSingleton();
    QuizManager.init(() -> fileProvider);
  }

  @AfterEach
  void teardown() throws Exception {
    mocks.close();
    resetQuizManagerSingleton();
  }

  /**
   * Resets the singleton instance of QuizManager using reflection.
   * This is necessary to ensure test isolation and prevent side effects
   * from the singleton state persisting between tests.
   */
  private void resetQuizManagerSingleton() throws Exception {
    Field instanceField = QuizManager.class.getDeclaredField("instance");
    instanceField.setAccessible(true);
    instanceField.set(null, null);
  }

  @Test
  @DisplayName("getRandomQuestion returns non-null")
  void randomQuestionNotNull() {
    QuizManager quizManager = QuizManager.getInstance();
    quizManager.loadQuestions("data/questions.json");
    assertNotNull(quizManager.getRandomQuestion());
  }

  @Test
  @DisplayName("getRandomQuestionFromCategory returns expected category")
  void randomQuestionFromCategoryOk() {
    QuizManager quizManager = QuizManager.getInstance();
    quizManager.loadQuestions("data/questions.json");
    Question q = quizManager.getRandomQuestionFromCategory(QuestionCategory.GEOGRAPHY);
    assertNotNull(q);
    assertEquals(QuestionCategory.GEOGRAPHY, q.getCategory());
  }

  @Test
  @DisplayName("absent category returns null")
  void categoryAbsentReturnsNull() {
    QuizManager quizManager = QuizManager.getInstance();
    quizManager.loadQuestions("data/questions.json");
    assertNull(quizManager.getRandomQuestionFromCategory(QuestionCategory.SCIENCE));
  }

  @Test
  @DisplayName("empty data file results in null responses")
  void emptyJsonFile() throws Exception {
    FileProvider empty = mock(FileProvider.class);
    when(empty.get("data/questions.json"))
        .thenReturn("[]".getBytes(StandardCharsets.UTF_8));
    resetQuizManagerSingleton();
    QuizManager.init(() -> empty);
    QuizManager quizManager = QuizManager.getInstance();
    quizManager.loadQuestions("data/questions.json");
    assertNull(quizManager.getRandomQuestion());
    assertNull(quizManager.getRandomQuestionFromCategory(QuestionCategory.GEOGRAPHY));
  }

  @Test
  @DisplayName("malformed JSON results in null responses")
  void malformedJson() throws Exception {
    FileProvider bad = mock(FileProvider.class);
    when(bad.get("data/questions.json"))
        .thenReturn("not json".getBytes(StandardCharsets.UTF_8));
    resetQuizManagerSingleton();
    QuizManager.init(() -> bad);
    QuizManager quizManager = QuizManager.getInstance();
    quizManager.loadQuestions("data/questions.json");
    assertNull(quizManager.getRandomQuestion());
    assertNull(quizManager.getRandomQuestionFromCategory(QuestionCategory.GEOGRAPHY));
  }
}