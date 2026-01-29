package minesweeper;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to preload and cache all game images.
 * This prevents lag during gameplay, especially on slow internet connections.
 */
public class ImagePreloader {
    private static final Map<String, Image> imageCache = new HashMap<>();
    private static boolean imagesPreloaded = false;

    /**
     * Preloads all images used in the Minesweeper game.
     * Images are cached to avoid reloading them multiple times.
     */
    public static void preloadAllImages() {
        if (imagesPreloaded) {
            return;
        }

        // Minesweeper basic images
        preloadImage("images/minesweeper-basic/mine-grey.png");
        preloadImage("images/minesweeper-basic/mine-red.png");
        preloadImage("images/minesweeper-basic/mine-misflagged.png");
        preloadImage("images/minesweeper-basic/flag.png");
        preloadImage("images/minesweeper-basic/cover.png");

        // Number images (0-8)
        for (int i = 0; i <= 8; i++) {
            preloadImage("images/minesweeper-basic/" + i + ".png");
        }

        // Face images
        preloadImage("images/minesweeper-basic/face-smile.png");
        preloadImage("images/minesweeper-basic/face-dead.png");
        preloadImage("images/minesweeper-basic/face-win.png");
        preloadImage("images/minesweeper-basic/face-O.png");

        // Digit images (0-9 and negative sign)
        for (int i = 0; i <= 9; i++) {
            preloadImage("images/digits/" + i + ".png");
        }
        preloadImage("images/digits/neg.png");

        imagesPreloaded = true;
    }

    /**
     * Preloads a single image and caches it.
     * @param imagePath The path to the image to preload.
     */
    private static void preloadImage(String imagePath) {
        if (!imageCache.containsKey(imagePath)) {
            Image image = new Image(imagePath);
            imageCache.put(imagePath, image);
        }
    }

    /**
     * Gets a cached image. If the image is not cached, it will be loaded and cached.
     * @param imagePath The path to the image.
     * @return The cached or newly loaded image.
     */
    public static Image getImage(String imagePath) {
        if (!imageCache.containsKey(imagePath)) {
            preloadImage(imagePath);
        }
        return imageCache.get(imagePath);
    }

    /**
     * Clears the image cache. Useful for freeing memory if needed.
     */
    public static void clearCache() {
        imageCache.clear();
        imagesPreloaded = false;
    }
}
