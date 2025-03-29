package edu.ntnu.idi.idatt.boardgame.ui.javafx;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.freeze.FreezeAction;
import edu.ntnu.idi.idatt.boardgame.actions.immunity.ImmunityAction;
import edu.ntnu.idi.idatt.boardgame.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.ui.style.TileStyleApplier;
import edu.ntnu.idi.idatt.boardgame.ui.style.TileStyleApplierFactory;

/**
 * JavaFX implementation of the TileStyleApplierFactory.
 * This factory creates JavaFX-specific style appliers for each action type.
 */
public class JavaFxTileStyleApplierFactory implements TileStyleApplierFactory {

    private static final JavaFxTileStyleApplierFactory INSTANCE = new JavaFxTileStyleApplierFactory();

    private final JavaFxLadderStyleApplier ladderStyleApplier = new JavaFxLadderStyleApplier();
    private final JavaFxFreezeStyleApplier freezeStyleApplier = new JavaFxFreezeStyleApplier();
    private final JavaFxImmunityStyleApplier immunityStyleApplier = new JavaFxImmunityStyleApplier();

    private JavaFxTileStyleApplierFactory() {
    }

    /**
     * Get the singleton instance of this factory.
     * 
     * @return The factory instance
     */
    public static JavaFxTileStyleApplierFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public TileStyleApplier getStyleApplier(TileAction action) {
        if (action instanceof LadderAction) {
            return ladderStyleApplier;
        } else if (action instanceof FreezeAction) {
            return freezeStyleApplier;
        } else if (action instanceof ImmunityAction) {
            return immunityStyleApplier;
        }

        // Default case - could return a default style applier or null
        return null;
    }
}