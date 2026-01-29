package minesweeper;

/**
 * Represents the difficulty level of the Minesweeper game.
 * Each difficulty level has a specific number of rows, columns, and mines.
 */
public enum Difficulty {
    BEGINNER("Beginner", 8, 8, 10, 30, 280, 360),
    INTERMEDIATE("Intermediate", 16, 16, 40, 30, 545, 640),
    EXPERT("Expert", 16, 32, 99, 25, 885, 570);

    /**
     * Converts a string to a Difficulty enum.
     *
     * @param s the string representation of the difficulty
     * @return the corresponding Difficulty enum, or null if no match is found
     */
    public static Difficulty fromString(String s) {
        return switch (s) {
            case "Beginner" -> BEGINNER;
            case "Intermediate" -> INTERMEDIATE;
            case "Expert" -> EXPERT;
            default -> null;
        };
    }

    private final String name;
    private final int rows;
    private final int cols;
    private final int mines;
    private final int tileSize;
    private final int screenWidth;
    private final int screenHeight;
    private final int totalSpaces;

    /**
     * Constructor for the Difficulty enum.
     *
     * @param name the name of the difficulty level
     * @param rows the number of rows in the game board
     * @param cols the number of columns in the game board
     * @param mines the number of mines in the game board
     * @param tileSize the size of each tile in the game board
     * @param screenWidth the width of the game screen
     * @param screenHeight the height of the game screen
     */
    Difficulty(String name, int rows, int cols, int mines, int tileSize, int screenWidth, int screenHeight) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.tileSize = tileSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.totalSpaces = rows * cols;
    }

    /**
     * Gets the name of the difficulty level.
     *
     * @return the name of the difficulty level
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the number of rows in the game board.
     *
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns in the game board.
     *
     * @return the number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Gets the number of mines in the game board.
     *
     * @return the number of mines
     */
    public int getMines() {
        return mines;
    }

    /**
     * Gets the size of each tile in the game board.
     *
     * @return the size of each tile
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * Gets the width of the game screen.
     *
     * @return the width of the game screen
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * Gets the height of the game screen.
     *
     * @return the height of the game screen
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * Gets the total number of spaces in the game board.
     *
     * @return the total number of spaces
     */
    public int getTotalSpaces() {
        return totalSpaces;
    }

    /**
     * Gets the total number of clicks required to win the game.
     *
     * @return the total number of clicks required to win
     */
    public int getTotalClicks() {
        return totalSpaces - mines;
    }

    /**
     * Returns the name of the difficulty level as a string.
     *
     * @return the name of the difficulty level
     */
    @Override
    public String toString() {
        return name;
    }
}