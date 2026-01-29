package minesweeper;

import javafx.scene.image.Image;

/**
 * Represents a space on the Minesweeper board.
 * Each space can either be a mine or an empty space with a number of adjacent mines.
 */
public class SpaceItem {
    private boolean mine;
    private boolean isFlagged;
    private boolean isRevealed;
    private int numAdjacentMines;

    /**
     * Constructor for the SpaceItem class.
     */
    public SpaceItem() {
        this.mine = false;
        this.isRevealed = false;
        this.numAdjacentMines = 0;
    }

    /**
     * Gets the image for an unrevealed mine (different from the revealed red mine image).
     * @return The image for an unrevealed mine.
     */
    static public Image getUnrevealedMineImage() {
        return new Image("images/minesweeper-basic/mine-grey.png");
    }

    /**
     * Gets the image for a revealed mine.
     * @return The image for a revealed mine.
     */
    static public Image getRevealedMineImage() {
        return new Image("images/minesweeper-basic/mine-red.png");
    }

    /**
     * Gets the image for a flag.
     * @return The image for a flag.
     */
    static public Image getFlagImage() {
        return new Image("images/minesweeper-basic/flag.png");
    }

    /**
     * Gets the image for a unrevealed space.
     * @return The image for a unrevealed space.
     */
    static public Image getCoverImage() {
        return new Image("images/minesweeper-basic/cover.png");
    }

    /**
     * Gets the image for a non-mine revealed space.
     * @return The image for a non-mine revealed space.
     */
    static public Image getNumImage(int num) {
        return new Image("images/minesweeper-basic/" + num + ".png");
    }

    /**
     * Gets the image for the revealed state of the space.
     * @return The image for the revealed state of the space.
     */
    public Image getRevealedImage() {
        if (this.mine) {
            return SpaceItem.getRevealedMineImage();
        } else {
            return SpaceItem.getNumImage(this.numAdjacentMines);
        }
    }

    /**
     * Checks if the space is a mine.
     * @return True if the space is a mine, false otherwise.
     */
    public boolean isMine() {
        return this.mine;
    }

    public void setMine(boolean isMine) {
        this.mine = isMine;
    }

    public boolean isFlagged() {
        return this.isFlagged;
    }

    /**
     * Checks if the space is revealed.
     * @return True if the space is revealed, false otherwise.
     */
    public boolean isRevealed() {
        return this.isRevealed;
    }

    /**
     * Sets the revealed state of the space.
     * @param isRevealed The new revealed state of the space.
     */
    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    /**
     * Gets the number of adjacent mines.
     * @return The number of adjacent mines.
     */
    public int getNumAdjacentMines() {
        return this.numAdjacentMines;
    }

    /**
     * Increments the number of adjacent mines by one.
     */
    public void addAdjacentMine() {
        this.numAdjacentMines++;
    }
}