package edu.ntnu.idi.idatt.boardgame.ui.javafx.player;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages pawn assets for the game UI.
 * <p>
 * This class is responsible for loading, caching, and providing access to
 * pawn images used in the game. It handles resource loading and maintains
 * a cache to avoid reloading the same images.
 * </p>
 */
public class PawnAssetManager {

    private static final Logger logger = Logger.getLogger(PawnAssetManager.class.getName());
    private static final Map<String, Image> pawnImageCache = new HashMap<>();
    private static final String DEFAULT_PAWN = "djinn.png";
    private static final String PAWN_RESOURCE_PATH = "/pawns/";

    private static PawnAssetManager instance;
    private final List<String> availablePawnImages;

    /**
     * Private constructor that initializes the available pawn images.
     */
    private PawnAssetManager() {
        this.availablePawnImages = loadAvailablePawnImages();
    }

    /**
     * Gets the singleton instance of the PawnAssetManager.
     *
     * @return the singleton instance
     */
    public static synchronized PawnAssetManager getInstance() {
        if (instance == null) {
            instance = new PawnAssetManager();
        }
        return instance;
    }

    /**
     * Gets a pawn image by its filename, loading it if necessary.
     *
     * @param pawnImageName the filename of the pawn image
     * @return the image, or the default image if the requested one couldn't be
     *         loaded
     */
    public Image getPawnImage(String pawnImageName) {
        
        if (!availablePawnImages.contains(pawnImageName)) {
            logger.log(Level.WARNING, "Requested pawn image ''{0}'' is not in the available list", pawnImageName);
            return getPawnImage(DEFAULT_PAWN);
        }

        
        if (pawnImageCache.containsKey(pawnImageName)) {
            return pawnImageCache.get(pawnImageName);
        }

        
        try {
            String resourcePath = PAWN_RESOURCE_PATH + pawnImageName;
            InputStream stream = getClass().getResourceAsStream(resourcePath);

            if (stream == null) {
                logger.log(Level.WARNING, "Could not find pawn image: {0}, using default", pawnImageName);
                return getPawnImage(DEFAULT_PAWN);
            }

            Image image = new Image(stream);
            pawnImageCache.put(pawnImageName, image);
            return image;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading pawn image: " + pawnImageName, e);

            if (!pawnImageName.equals(DEFAULT_PAWN)) {
                return getPawnImage(DEFAULT_PAWN);
            }
            return null; 
        }
    }

    /**
     * Gets the names of all available pawn images.
     *
     * @return a list of available pawn image filenames
     */
    public List<String> getAvailablePawnImages() {
        return new ArrayList<>(availablePawnImages);
    }

    /**
     * Checks if a pawn image with the given name is available.
     *
     * @param pawnImageName the name to check
     * @return true if the pawn image is available, false otherwise
     */
    public boolean isPawnImageAvailable(String pawnImageName) {
        return availablePawnImages.contains(pawnImageName);
    }

    /**
     * Loads the list of available pawn images.
     * <p>
     * In a production environment, this could scan the resources directory,
     * but for simplicity, we're using a static list.
     * </p>
     *
     * @return a list of available pawn image filenames
     */
    private List<String> loadAvailablePawnImages() {
        List<String> pawns = new ArrayList<>();

        pawns.add(DEFAULT_PAWN);
        pawns.add("drakkar-dragon.png");
        pawns.add("eight-ball.png");
        pawns.add("horned-reptile.png");
        pawns.add("ninja-velociraptor.png");
        pawns.add("sly.png");
        pawns.add("smitten.png");
        pawns.add("snake-bite.png");
        pawns.add("spectacle-lenses.png");
        pawns.add("toad-teeth.png");
        pawns.add("triton-head.png");

        List<String> validPawns = new ArrayList<>();
        for (String pawn : pawns) {
            InputStream stream = getClass().getResourceAsStream(PAWN_RESOURCE_PATH + pawn);
            if (stream != null) {
                validPawns.add(pawn);
                try {
                    stream.close();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error closing stream for " + pawn, e);
                }
            } else {
                logger.log(Level.WARNING, "Pawn image not found in resources: {0}", pawn);
            }
        }

        logger.log(Level.INFO, "Loaded {0} pawn images", validPawns.size());
        return validPawns;
    }

    /**
     * Gets the default pawn image name.
     *
     * @return the default pawn image name
     */
    public String getDefaultPawnImageName() {
        return DEFAULT_PAWN;
    }

    /**
     * Clears the image cache to free memory.
     */
    public void clearCache() {
        pawnImageCache.clear();
        logger.info("Pawn image cache cleared");
    }
}
