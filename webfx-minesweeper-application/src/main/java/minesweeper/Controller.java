package minesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.*;

/**
 * Controller class for the Minesweeper game. Handles all the game logic and states.
 */
public class Controller {
    private Difficulty difficulty;
    private int clickedSpaces = 0;
    private int flaggedMines = 0;
    private int time = 0;

    private boolean gameStarted = false;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean gamePaused = false;

    private final ImageView[] minesLeft = new ImageView[3];
    private final ImageView[] timeElapsed = new ImageView[3];
    private ImageView smileyImage;
    private VBox highScoreInput;
    private Text highScoreText;

    private final Map<ImageView, Pair<Integer, Integer>> boardMap;
    private final Map<Pair<Integer, Integer>, ImageView> coordMap;

    private SpaceItem[][] boardState;
    private Set<Pair<Integer, Integer>> isMineMap;
    private Random rand;
    private final Timeline timeline;

    /**
     * Constructor for the Controller class.
     */
    public Controller() {
        this.boardMap = new HashMap<>(); // Image view stays the same, so no need to reinitialize every game
        this.coordMap = new HashMap<>();

        // Initialize the timer
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> this.updateTimeElapsed()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Sets up the Minesweeper board.
     */
    public void setup() {
        // Reset the game state
        this.clickedSpaces = 0;
        this.flaggedMines = 0;
        this.gameStarted = false;
        this.gameOver = false;
        this.gameWon = false;
        this.gamePaused = false;
        this.rand = new Random();
        this.timeline.stop();

        this.time = -1;
        this.updateTimeElapsed();

        final int rows = this.difficulty.getRows();
        final int cols = this.difficulty.getCols();

        // Initialize the default board state
        this.boardState = new SpaceItem[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.boardState[row][col] = new SpaceItem();
            }
        }

        this.setNumMinesLeft(this.difficulty.getMines());
        this.smileyImage.setImage(new Image("images/minesweeper-basic/face-smile.png"));
        for (ImageView imageView : this.boardMap.keySet()) {
            imageView.setImage(SpaceItem.getCoverImage());
        }
    }

    /**
     * Pauses the game if possible.
     */
    public void pauseGame() {
        if (this.gameOver || this.gameWon || !this.gameStarted || this.gamePaused) {
            return;
        }

        this.gamePaused = true;
        this.timeline.pause();
    }

    /**
     * Resumes the game if previously paused.
     */
    public void resumeGame() {
        if (this.gameOver || this.gameWon || !this.gameStarted || !this.gamePaused) {
            return;
        }

        this.gamePaused = false;
        this.timeline.play();
    }

    /**
     * Clears the board map.
     */
    public void clearBoardMap() {
        this.boardMap.clear();
        this.coordMap.clear();
    }

    /**
     * Prints the board to the console.
     */
    private void printBoard() {
        final int rows = this.difficulty.getRows();
        final int cols = this.difficulty.getCols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print((this.boardState[i][j].isMine() ? "X" : this.boardState[i][j].getNumAdjacentMines()) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Updates the time elapsed and updates the header display.
     */
    public void updateTimeElapsed() {
        this.time++;
        if (this.time > 999) {
            this.time = 999;
        }

        int hundreds = this.time / 100;
        int tens = (this.time % 100) / 10;
        int ones = this.time % 10;

        this.timeElapsed[0].setImage(new Image("images/digits/" + hundreds + ".png"));
        this.timeElapsed[1].setImage(new Image("images/digits/" + tens + ".png"));
        this.timeElapsed[2].setImage(new Image("images/digits/" + ones + ".png"));
    }

    /**
     * Sets the number of mines left in the header display.
     *
     * @param numMinesLeft The number of mines left.
     */
    private void setNumMinesLeft(int numMinesLeft) {
        int hundreds = numMinesLeft / 100;
        int tens = Math.abs((numMinesLeft % 100) / 10);
        int ones = Math.abs(numMinesLeft % 10);
        if (numMinesLeft < 0) {
            this.minesLeft[0].setImage(new Image("images/digits/neg.png"));
        } else {
            this.minesLeft[0].setImage(new Image("images/digits/" + hundreds + ".png"));
        }
        this.minesLeft[1].setImage(new Image("images/digits/" + tens + ".png"));
        this.minesLeft[2].setImage(new Image("images/digits/" + ones + ".png"));
    }

    /**
     * Updates the number of adjacent mines for the given cell with a mine.
     *
     * @param row The row of the cell with the mine.
     * @param col The column of the cell with the mine.
     */
    private void updateMineNeighbors(int row, int col) {
        int maxRow = this.difficulty.getRows();
        int maxCol = this.difficulty.getCols();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < maxRow && j >= 0 && j < maxCol && !(i == row && j == col)) {
                    if (!this.boardState[i][j].isMine()) {
                        this.boardState[i][j].addAdjacentMine();
                    }
                }
            }
        }
    }

    /**
     * Adds mines to the board.
     */
    private void addMines(ImageView clickImage) {
        if (this.isMineMap == null) {
            this.isMineMap = new HashSet<>();
        }
        this.isMineMap.clear();

        Pair<Integer, Integer> clickCoords = this.boardMap.get(clickImage);
        final int clickRow = clickCoords.getKey();
        final int clickCol = clickCoords.getValue();
        for (int i = 0; i < this.difficulty.getMines(); i++) {
            int row = 0;
            int col = 0;
            do {
                row = this.rand.nextInt(this.difficulty.getRows());
                col = this.rand.nextInt(this.difficulty.getCols());
            }
            while (this.isMineMap.contains(new Pair<>(row, col))
                    ||
                    // Ensure that the first click is not a mine and its neighbors are not mines (so the first click is 0)
                    ((row == clickRow - 1 || row == clickRow || row == clickRow + 1) &&
                    (col == clickCol - 1 || col == clickCol || col == clickCol + 1)));

            this.isMineMap.add(new Pair<>(row, col));
            this.boardState[row][col].setMine(true);

            // Update the number of adjacent mines for each space
            this.updateMineNeighbors(row, col);
        }

        // Start the timer
        this.timeline.play();
        printBoard();
    }

    /**
     * Adds an ImageView and its coordinates to the board map.
     *
     * @param imageView The ImageView to add.
     * @param coords    The coordinates of the ImageView.
     */
    public void addToBoardMap(ImageView imageView, Pair<Integer, Integer> coords) {
        this.boardMap.put(imageView, coords);
        this.coordMap.put(coords, imageView);
    }

    /**
     * Gets the ImageViews representing the number of mines left.
     *
     * @return An array of ImageViews.
     */
    public ImageView[] getMinesLeft() {
        return this.minesLeft;
    }

    /**
     * Gets the ImageViews representing the elapsed time.
     *
     * @return An array of ImageViews.
     */
    public ImageView[] getTimeElapsed() {
        return this.timeElapsed;
    }

    /**
     * Gets the time elapsed in seconds.
     *
     * @return The time elapsed in seconds.
     */
    public int getTimeElapsedSeconds() {
        return this.time;
    }

    /**
     * Gets the difficulty level of the Minesweeper game.
     *
     * @return The difficulty level.
     */
    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    /**
     * Sets the difficulty level of the Minesweeper game.
     *
     * @param difficulty The difficulty level.
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Sets the smiley image.
     *
     * @param smileyImage The ImageView representing the smiley image.
     */
    public void setSmileyImage(ImageView smileyImage) {
        this.smileyImage = smileyImage;
    }

    /**
     * Sets the high score input VBox.
     *
     * @param highScoreInput The VBox representing the high score input.
     */
    public void setHighScoreInput(VBox highScoreInput, Text highScoreText) {
        this.highScoreInput = highScoreInput;
        this.highScoreText = highScoreText;
    }

    /**
     * Sets the game state to game over and reveals all mines.
     */
    private void setGameOver() {
        // Reveal all not found mines
        this.gameOver = true;
        for (Map.Entry<ImageView, Pair<Integer, Integer>> entry : this.boardMap.entrySet()) {
            ImageView imageView = entry.getKey();
            Pair<Integer, Integer> coords = entry.getValue();
            SpaceItem space = this.boardState[coords.getKey()][coords.getValue()];
            if (space.isMine() && !space.isRevealed() && !space.isFlagged()) {
                imageView.setImage(SpaceItem.getUnrevealedMineImage());
            }
            else if (space.isFlagged() && !space.isMine()) {
                imageView.setImage(new Image("images/minesweeper-basic/mine-misflagged.png"));
            }
        }
        this.smileyImage.setImage(new Image("images/minesweeper-basic/face-dead.png"));
        this.timeline.stop();
    }

    /**
     * Sets the game state to game won.
     */
    private void setGameWon() {
        this.gameWon = true;
        this.smileyImage.setImage(new Image("images/minesweeper-basic/face-win.png"));
        this.timeline.stop();

        if (HighScore.isHighScore(this.time, this.difficulty)) {
            this.highScoreText.setText("You beat the high score for " + difficulty.getName() + "! Enter your name:");
            this.highScoreInput.setVisible(true);
        }
    }

    /**
     * Handles the event when a space is clicked.
     *
     * @param imageView The ImageView representing the clicked space.
     */
    public void onSpaceClicked(ImageView imageView, boolean leftClick) {
        if (this.gameOver || this.gameWon) {
            return;
        }
        this.spaceClicked(imageView, leftClick);
    }

    /**
     * Handles the event when the mouse is pressed on a space.
     *
     * @param imageView The ImageView representing the space.
     * @param leftClick True if the left mouse button was clicked, false otherwise.
     */
    protected void spaceClickDown(ImageView imageView, boolean leftClick) {
        if (this.gameOver || this.gameWon || !leftClick) {
            return;
        }
        this.smileyImage.setImage(new Image("images/minesweeper-basic/face-O.png"));

        Pair<Integer, Integer> coords = this.boardMap.get(imageView);
        SpaceItem space = this.boardState[coords.getKey()][coords.getValue()];
        if (!space.isRevealed() && !space.isFlagged()) {
            imageView.setImage(SpaceItem.getNumImage(0));
        }
    }

    /**
     * Handles the event when the mouse is released from a space.
     *
     * @param imageView The ImageView representing the space.
     */
    protected void spaceClickUp(ImageView imageView) {
        if (this.gameOver || this.gameWon) {
            return;
        }
        this.smileyImage.setImage(new Image("images/minesweeper-basic/face-smile.png"));

        Pair<Integer, Integer> coords = this.boardMap.get(imageView);
        SpaceItem space = this.boardState[coords.getKey()][coords.getValue()];
        if (!space.isRevealed() && !space.isFlagged()) {
            imageView.setImage(SpaceItem.getCoverImage());
        }
    }

    /**
     * Handles the logic for placing a flag on a space.
     *
     * @param space     The space to place the flag on.
     * @param imageView The ImageView representing the space.
     */
    private void handleFlag(SpaceItem space, ImageView imageView) {
        // Don't allow more than 99 flags + mines if trying to place a flag (to avoid going below -99)
        if ((this.flaggedMines >= this.difficulty.getMines() + 99 && !space.isFlagged()) || space.isRevealed()) {
            return;
        }

        space.setFlagged(!space.isFlagged());
        if (space.isFlagged()) {
            imageView.setImage(SpaceItem.getFlagImage());
            this.flaggedMines++;
        } else {
            imageView.setImage(SpaceItem.getCoverImage());
            this.flaggedMines--;
        }
        this.setNumMinesLeft(this.difficulty.getMines() - this.flaggedMines);
    }

    /**
     * Recursively reveals spaces on the board while adjacent spaces have no mines.
     *
     * @param row The row of the space to reveal.
     * @param col The column of the space to reveal.
     */
    private void recursiveReveal(int row, int col) {
        if (row < 0 || row >= this.difficulty.getRows() || col < 0 || col >= this.difficulty.getCols()) {
            return;
        }

        SpaceItem space = this.boardState[row][col];
        if (space.isRevealed() || space.isFlagged()) {
            return;
        }

        if (this.revealSpace(space, this.coordMap.get(new Pair<>(row, col)))) {
            return;
        }

        if (space.getNumAdjacentMines() == 0) {
            recursiveReveal(row - 1, col - 1);
            recursiveReveal(row - 1, col);
            recursiveReveal(row - 1, col + 1);
            recursiveReveal(row, col - 1);
            recursiveReveal(row, col + 1);
            recursiveReveal(row + 1, col - 1);
            recursiveReveal(row + 1, col);
            recursiveReveal(row + 1, col + 1);
        }
    }

    /**
     * Reveals the space and checks if the game is over or won.
     *
     * @param space The space to check.
     * @return True if the game is over, false otherwise.
     */
    private boolean revealSpace(SpaceItem space, ImageView imageView) {
        space.setRevealed(true);
        imageView.setImage(space.getRevealedImage());

        // Check if the game is over
        if (space.isMine()) {
            setGameOver();
            return true;
        }

        // Check if the game is won
        this.clickedSpaces++;
        if (this.clickedSpaces == this.difficulty.getTotalClicks()) {
            setGameWon();
            return true;
        }

        return false;
    }

    /**
     * Handles the logic for when a revealed number is clicked.
     *
     * @param row The row of the clicked space.
     * @param col The column of the clicked space.
     */
    private void revealedNumberClicked(int row, int col) {
        if (!this.boardState[row][col].isRevealed() || this.boardState[row][col].getNumAdjacentMines() == 0) {
            return;
        }

        int numFlags = 0;
        int maxRow = this.difficulty.getRows();
        int maxCol = this.difficulty.getCols();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < maxRow && j >= 0 && j < maxCol) {
                    if (this.boardState[i][j].isFlagged()) {
                        numFlags++;
                    }
                }
            }
        }
        if (numFlags != this.boardState[row][col].getNumAdjacentMines()) {
            return;
        }

        // Reveal all adjacent spaces if the correct number of flags are placed
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < maxRow && j >= 0 && j < maxCol) {
                    if (!this.boardState[i][j].isRevealed() && !this.boardState[i][j].isFlagged()) {
                        this.recursiveReveal(i, j);
                    }
                }
            }
        }

    }

    /**
     * Handles the logic for when a space is clicked.
     *
     * @param imageView The ImageView representing the clicked space.
     * @param leftClick True if the left mouse button was clicked, false otherwise.
     */
    protected void spaceClicked(ImageView imageView, boolean leftClick) {
        // Get the space at the clicked coordinates
        Pair<Integer, Integer> coords = this.boardMap.get(imageView);
        SpaceItem space = this.boardState[coords.getKey()][coords.getValue()];

        if (!leftClick) {
            this.handleFlag(space, imageView);
            return;
        }

        if (space.isFlagged()) {
            return;
        }

        // Start the game if first left click
        if (!this.gameStarted) {
            this.gameStarted = true;
            this.addMines(imageView);
        }

        this.revealedNumberClicked(coords.getKey(), coords.getValue());
        if (space.isRevealed()) {
            return;
        }

        this.recursiveReveal(coords.getKey(), coords.getValue());
    }
}