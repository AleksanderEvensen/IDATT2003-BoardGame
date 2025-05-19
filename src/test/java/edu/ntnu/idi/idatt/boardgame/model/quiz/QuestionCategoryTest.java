package edu.ntnu.idi.idatt.boardgame.model.quiz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for the QuestionCategory enum.
 */
public class QuestionCategoryTest {

    @Test
    public void testGetDisplayName() {
        assertEquals("General Knowledge", QuestionCategory.GENERAL_KNOWLEDGE.getDisplayName());
        assertEquals("Science", QuestionCategory.SCIENCE.getDisplayName());
        assertEquals("History", QuestionCategory.HISTORY.getDisplayName());
        assertEquals("Geography", QuestionCategory.GEOGRAPHY.getDisplayName());
        assertEquals("Entertainment", QuestionCategory.ENTERTAINMENT.getDisplayName());
        assertEquals("Art", QuestionCategory.ART.getDisplayName());
        assertEquals("Sports", QuestionCategory.SPORTS.getDisplayName());
        assertEquals("Random", QuestionCategory.RANDOM.getDisplayName());
    }

    @Test
    public void testEnumValues() {
        QuestionCategory[] categories = QuestionCategory.values();
        assertEquals(8, categories.length);
    }
}
