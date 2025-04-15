package edu.ntnu.idi.idatt.boardgame.ui.javafx.player;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import javafx.scene.image.Image;

/**
 * Tests for the PawnAssetManager class.
 */
class PawnAssetManagerTest {

    /**
     * Test that the singleton pattern works correctly.
     */
    @Test
    void testSingletonInstance() {
        PawnAssetManager instance1 = PawnAssetManager.getInstance();
        PawnAssetManager instance2 = PawnAssetManager.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2, "Multiple calls to getInstance should return the same instance");
    }

    /**
     * Test that the available pawns list is not empty.
     */
    @Test
    void testAvailablePawnsNotEmpty() {
        PawnAssetManager manager = PawnAssetManager.getInstance();

        assertFalse(manager.getAvailablePawnImages().isEmpty(),
                "Available pawns list should not be empty");
    }

    /**
     * Test that the default pawn image name is available.
     */
    @Test
    void testDefaultPawnAvailable() {
        PawnAssetManager manager = PawnAssetManager.getInstance();
        String defaultPawn = manager.getDefaultPawnImageName();

        assertNotNull(defaultPawn, "Default pawn name should not be null");
        assertTrue(manager.isPawnImageAvailable(defaultPawn),
                "Default pawn should be available");
    }
}
