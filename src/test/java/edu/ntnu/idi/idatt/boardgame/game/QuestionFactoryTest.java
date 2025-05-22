package edu.ntnu.idi.idatt.boardgame.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.ntnu.idi.idatt.boardgame.model.entities.Question;
import edu.ntnu.idi.idatt.boardgame.model.entities.QuestionCategory;
import edu.ntnu.idi.idatt.boardgame.model.factories.QuestionFactory;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionFactoryTest {

  @Test
  void createQuestion_shouldCreateQuestionFromValidJson() {
    // Create test JSON
    String json = """
        {
          "question": "What is 2+2?",
          "category": "GENERAL_KNOWLEDGE",
          "answers": {
            "1": "3",
            "2": "4",
            "3": "5",
            "4": "6"
          },
          "correctAnswerIndex": 2
        }
        """;

    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

    // Create question using factory
    Question question = QuestionFactory.createQuestion(jsonObject);

    // Assertions
    assertNotNull(question);
    assertEquals("What is 2+2?", question.getQuestion());
    assertEquals(QuestionCategory.GENERAL_KNOWLEDGE, question.getCategory());

    List<String> expectedAnswers = List.of("3", "4", "5", "6");
    assertEquals(expectedAnswers, question.getAnswers());
    assertEquals("4", question.getCorrectAnswer());
    assertEquals(2, question.getCorrectAnswerIndex());
  }

  @Test
  void createQuestion_shouldHandleMinimalJson() {
    // Create minimal JSON with only required fields
    String json = """
        {
          "question": "Simple question",
          "category": "SCIENCE",
          "answers": {
            "1": "Answer"
          },
          "correctAnswerIndex": 1
        }
        """;

    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

    Question question = QuestionFactory.createQuestion(jsonObject);

    assertNotNull(question);
    assertEquals("Simple question", question.getQuestion());
    assertEquals(QuestionCategory.SCIENCE, question.getCategory());
    assertEquals(1, question.getAnswers().size());
    assertEquals("Answer", question.getCorrectAnswer());
  }
}
