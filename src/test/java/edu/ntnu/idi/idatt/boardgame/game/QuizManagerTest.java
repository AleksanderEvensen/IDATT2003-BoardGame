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
  private QuizManager quizManager;

  @BeforeEach
  void setup() {
    mocks = MockitoAnnotations.openMocks(this);
    fileProvider = mock(FileProvider.class);
    when(fileProvider.get("data/questions.json"))
        .thenReturn(SAMPLE_JSON.getBytes(StandardCharsets.UTF_8));
    quizManager = new QuizManager(fileProvider);
  }

  @AfterEach
  void teardown() throws Exception {
    mocks.close();
  }

  @Test
  @DisplayName("getRandomQuestion returns non-null")
  void randomQuestionNotNull() {
    assertNotNull(quizManager.getRandomQuestion());
  }

  @Test
  @DisplayName("getRandomQuestionFromCategory returns expected category")
  void randomQuestionFromCategoryOk() {
    Question q = quizManager.getRandomQuestionFromCategory(QuestionCategory.GEOGRAPHY);
    assertNotNull(q);
    assertEquals(QuestionCategory.GEOGRAPHY, q.getCategory());
  }

  @Test
  @DisplayName("absent category returns null")
  void categoryAbsentReturnsNull() {
    assertNull(quizManager.getRandomQuestionFromCategory(QuestionCategory.SCIENCE));
  }

  @Test
  @DisplayName("empty data file results in null responses")
  void emptyJsonFile() {
    FileProvider empty = mock(FileProvider.class);
    when(empty.get("data/questions.json"))
        .thenReturn("[]".getBytes(StandardCharsets.UTF_8));
    QuizManager mgr = new QuizManager(empty);
    assertNull(mgr.getRandomQuestion());
    assertNull(mgr.getRandomQuestionFromCategory(QuestionCategory.GEOGRAPHY));
  }

  @Test
  @DisplayName("malformed JSON results in null responses")
  void malformedJson() {
    FileProvider bad = mock(FileProvider.class);
    when(bad.get("data/questions.json"))
        .thenReturn("not json".getBytes(StandardCharsets.UTF_8));
    QuizManager mgr = new QuizManager(bad);
    assertNull(mgr.getRandomQuestion());
    assertNull(mgr.getRandomQuestionFromCategory(QuestionCategory.GEOGRAPHY));
  }
}
