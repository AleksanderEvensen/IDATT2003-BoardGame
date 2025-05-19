package edu.ntnu.idi.idatt.boardgame.model.quiz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Question class.
 */
public class QuestionTest {

    private Question question;
    private HashMap<Integer, String> answers;

    @BeforeEach
    public void setUp() {
        answers = new HashMap<>();
        answers.put(1, "Paris");
        answers.put(2, "London");
        answers.put(3, "Berlin");
        answers.put(4, "Rome");

        question = new Question(
                "What is the capital of France?",
                QuestionCategory.GEOGRAPHY,
                answers,
                1);
    }

    @Test
    public void testGetQuestion() {
        assertEquals("What is the capital of France?", question.getQuestion());
    }

    @Test
    public void testGetCategory() {
        assertEquals(QuestionCategory.GEOGRAPHY, question.getCategory());
    }

    @Test
    public void testGetCategoryName() {
        assertEquals("Geography", question.getCategoryName());
    }

    @Test
    public void testGetAnswers() {
        List<String> answerList = question.getAnswers();
        assertEquals(4, answerList.size());
        assertTrue(answerList.contains("Paris"));
        assertTrue(answerList.contains("London"));
        assertTrue(answerList.contains("Berlin"));
        assertTrue(answerList.contains("Rome"));
    }

    @Test
    public void testGetCorrectAnswerIndex() {
        assertEquals(1, question.getCorrectAnswerIndex());
    }

    @Test
    public void testGetCorrectAnswer() {
        assertEquals("Paris", question.getCorrectAnswer());
    }
}
