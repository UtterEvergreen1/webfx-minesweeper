package minesweeper;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Main application class for the Minesweeper game.
 * Sets up the game board and handles the user interface.
 */
public class MainApplication extends Application {
    private final Controller controller = new Controller();
    private Stage mainStage;
    private Scene mainScene;
    private StackPane root;
    private VBox gameRoot;
    private VBox menu;
    private TextField nameField;
    private VBox highScoreInput;
    private Text highScoreText;
    private VBox highScoreDisplay;
    private Text highScoreDisplayText;
    private HBox header;

    /**
     * Creates the header for the Minesweeper game.
     */
    private void makeHeader() {
        // Header of Minesweeper
        this.header = new HBox();
        this.header.setSpacing(10);
        this.header.setAlignment(Pos.BOTTOM_CENTER);
        this.add3DBorder(header);

        // Add digits for mines left
        this.makeDigits(header, this.controller.getMinesLeft(), true);

        // Smiley face
        Image smileyImage = ImagePreloader.getImage("images/minesweeper-basic/face-smile.png");
        ImageView smileyImageView = new ImageView(smileyImage);
        this.controller.setSmileyImage(smileyImageView);
        smileyImageView.setFitWidth(52);
        smileyImageView.setFitHeight(52);
        smileyImageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.resetGame();
            }
        });
        this.header.getChildren().add(smileyImageView);

        // Add digits for time elapsed
        this.makeDigits(this.header, this.controller.getTimeElapsed(), false);
    }

    /**
     * Creates the menu for selecting the difficulty level.
     */
    private void makeMenu() {
        // Create the difficulty selection menu
        this.menu = new VBox(10);
        this.menu.setBackground(new Background(new BackgroundFill(
            javafx.scene.paint.Color.rgb(0, 0, 0, 0.8),
            null, null)));
        this.menu.setPadding(new javafx.geometry.Insets(20));
        this.menu.setAlignment(javafx.geometry.Pos.CENTER);

        // Text for the menu "Choose a difficulty"
        Text text = new Text("Choose a difficulty");
        text.setFill(javafx.scene.paint.Color.WHITE);
        text.setFont(javafx.scene.text.Font.font(20));

        Button beginnerButton = new Button("Beginner");
        Button intermediateButton = new Button("Intermediate");
        Button expertButton = new Button("Expert");

        // Dark mode button styling - dark backgrounds with bright text
        styleButton(beginnerButton, javafx.scene.paint.Color.rgb(0, 170, 0), javafx.scene.paint.Color.rgb(0, 255, 0));
        styleButton(intermediateButton, javafx.scene.paint.Color.rgb(204, 153, 0), javafx.scene.paint.Color.rgb(255, 204, 0));
        styleButton(expertButton, javafx.scene.paint.Color.rgb(204, 0, 0), javafx.scene.paint.Color.rgb(255, 0, 0));

        beginnerButton.setOnAction(event -> setDifficulty(Difficulty.BEGINNER));
        intermediateButton.setOnAction(event -> setDifficulty(Difficulty.INTERMEDIATE));
        expertButton.setOnAction(event -> setDifficulty(Difficulty.EXPERT));

        this.menu.getChildren().addAll(text, beginnerButton, intermediateButton, expertButton);
    }

    private void styleButton(Button button, javafx.scene.paint.Color bgColor, javafx.scene.paint.Color borderColor) {
        button.setFont(javafx.scene.text.Font.font(16));
        button.setBackground(new Background(new BackgroundFill(bgColor, new CornerRadii(0), null)));
        button.setBorder(new Border(new BorderStroke(
            borderColor,
            BorderStrokeStyle.SOLID,
            new CornerRadii(0),
            new BorderWidths(2))));
        button.setTextFill(javafx.scene.paint.Color.WHITE);
    }

    /**
     * Creates the input for submitting a high score.
     */
    private void makeHighScoreInput() {
        // Create the high score input VBox
        this.highScoreInput = new VBox(10);
        this.highScoreInput.setBackground(new Background(new BackgroundFill(
            javafx.scene.paint.Color.rgb(0, 0, 0, 0.8),
            null, null)));
        this.highScoreInput.setPadding(new javafx.geometry.Insets(20));
        this.highScoreInput.setAlignment(javafx.geometry.Pos.CENTER);

        this.highScoreText = new Text();
        this.highScoreText.setFill(javafx.scene.paint.Color.WHITE);
        this.highScoreText.setFont(javafx.scene.text.Font.font(20));
        this.highScoreText.setWrappingWidth(250);
        this.highScoreText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        this.nameField = new TextField();
        this.nameField.setPromptText("Your name");
        this.nameField.setBackground(new Background(new BackgroundFill(
            javafx.scene.paint.Color.rgb(51, 51, 51),
            null, null)));
        // Note: TextField text color needs to be set via style as there's no direct API
        this.nameField.setStyle("--body-text-color: white;");


        Button submitButton = new Button("Submit");
        styleButton(submitButton, javafx.scene.paint.Color.rgb(0, 102, 204), javafx.scene.paint.Color.rgb(0, 136, 255));

        submitButton.setOnAction(event -> {
            this.updateHighScores();
        });

        this.controller.setHighScoreInput(this.highScoreInput, this.highScoreText);
        this.highScoreInput.getChildren().addAll(this.highScoreText, this.nameField, submitButton);
        this.highScoreInput.setVisible(false);
    }

    /**
     * Creates the display for showing the high scores.
     */
    private void makeHighScoreDisplay() {
        // Create the high score display VBox
        this.highScoreDisplay = new VBox(10);
        this.highScoreDisplay.setBackground(new Background(new BackgroundFill(
            javafx.scene.paint.Color.rgb(0, 0, 0, 0.8),
            null, null)));
        this.highScoreDisplay.setPadding(new javafx.geometry.Insets(20));
        this.highScoreDisplay.setAlignment(javafx.geometry.Pos.CENTER);

        Text text = new Text("High Scores");
        text.setFill(javafx.scene.paint.Color.WHITE);
        text.setFont(javafx.scene.text.Font.font("Monospaced", 20));
        this.highScoreDisplay.getChildren().add(text);

        this.highScoreDisplayText = new Text(HighScore.getHighScoreText());
        this.highScoreDisplayText.setFill(javafx.scene.paint.Color.WHITE);
        this.highScoreDisplayText.setFont(javafx.scene.text.Font.font("Monospaced", 12));
        this.highScoreDisplay.getChildren().add(this.highScoreDisplayText);

        HBox highScoreButtons = new HBox(10);
        highScoreButtons.setAlignment(javafx.geometry.Pos.CENTER);
        this.highScoreDisplay.getChildren().add(highScoreButtons);

        // Create a button to reset high scores
        Button resetButton = new Button("Reset");
        styleButton(resetButton, javafx.scene.paint.Color.rgb(204, 0, 0), javafx.scene.paint.Color.rgb(255, 0, 0));
        resetButton.setOnAction(event -> {
            HighScore.resetHighScores();
            this.highScoreDisplayText.setText("No high scores yet!");
        });
        highScoreButtons.getChildren().add(resetButton);

        // Create a button to close the high score display
        Button closeButton = new Button("Close");
        styleButton(closeButton, javafx.scene.paint.Color.rgb(85, 85, 85), javafx.scene.paint.Color.rgb(136, 136, 136));
        closeButton.setOnAction(event -> this.toggleHighScores());
        highScoreButtons.getChildren().add(closeButton);

        this.highScoreDisplay.setVisible(false);
    }

    /**
     * Creates the root pane for the game.
     */
    private void makeGameRoot() {
        // Combines the header and game area into a single VBox as the root pane
        this.gameRoot = new VBox();
        this.gameRoot.setSpacing(10);
        this.gameRoot.setVisible(false);
        // Border with outward offset using negative insets
        this.gameRoot.setBorder(new Border(new BorderStroke(
            javafx.scene.paint.Color.rgb(223, 223, 223),
            javafx.scene.paint.Color.rgb(136, 136, 136),
            javafx.scene.paint.Color.rgb(136, 136, 136),
            javafx.scene.paint.Color.rgb(223, 223, 223),
            BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
            null, new BorderWidths(4), null)));
        this.gameRoot.setPadding(new javafx.geometry.Insets(10));
        this.gameRoot.setBackground(new Background(new BackgroundFill(
            javafx.scene.paint.Color.rgb(153, 153, 153),
            null, null)));
        this.gameRoot.setAlignment(javafx.geometry.Pos.CENTER);

        this.makeHeader();
        this.gameRoot.getChildren().add(this.header);

        // Create the default game board
        Pane boardPane = this.setupBoard(0, 0, 1); // Initially empty
        this.gameRoot.getChildren().add(boardPane);

        this.makeMenu();
        this.makeHighScoreInput();
        this.makeHighScoreDisplay();
    }

    /**
     * Creates the main scene for the application.
     */
    private void makeScene() {
        // Create scene without fixed size - let root control sizing
        this.mainScene = new Scene(this.root);
        this.mainScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.H) {
                toggleHighScores();
            }
        });
        // Set dark background
        this.mainScene.setFill(javafx.scene.paint.Color.rgb(30, 31, 34));
        this.mainStage.setScene(this.mainScene);
        this.mainStage.setTitle("Minesweeper");
        this.mainStage.show();
    }

    /**
     * Combines the game board, menu, and high score input into a single stacked root pane.
     */
    private void makeRoot() {
        // Overlay the menu and high score input on top of the game board with a StackPane
        this.root = new StackPane();
        this.root.setAlignment(javafx.geometry.Pos.CENTER);
        this.root.getChildren().addAll(this.gameRoot, this.menu, this.highScoreInput, this.highScoreDisplay);
        // Set background color on the root StackPane
        this.root.setBackground(new Background(new BackgroundFill(
            javafx.scene.paint.Color.rgb(30, 31, 34),
            null, null)));
    }

    /**
     * Starts the application and sets up the game board.
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        this.mainStage = stage;
        HighScore.readHighScores();

        this.makeGameRoot();
        this.makeRoot();
        this.makeScene();
    }

    /**
     * Updates the high scores with the player's name and time.
     */
    private void updateHighScores() {
        String playerName = this.nameField.getText();
        // Handle high score submission
        this.highScoreInput.setVisible(false);
        this.menu.setVisible(true);
        this.saveHighScore(playerName, this.controller.getTimeElapsedSeconds(), this.controller.getDifficulty());
        this.highScoreDisplayText.setText(HighScore.getHighScoreText());
    }

    /**
     * Toggles the visibility of the high score display.
     */
    private void toggleHighScores() {
        if (this.highScoreDisplay.isVisible()) {
            this.highScoreDisplay.setVisible(false);
            this.controller.resumeGame();
            return;
        }

        this.controller.pauseGame();
        this.highScoreDisplay.setVisible(true);
    }

    /**
     * Saves the high score to the high score list.
     * @param playerName The name of the player.
     * @param score The score of the player.
     * @param difficulty The difficulty level of the game.
     */
    private void saveHighScore(String playerName, int score, Difficulty difficulty) {
        HighScore.addHighScore(playerName, score, difficulty);
        HighScore.writeHighScores();
    }

    /**
     * Sets up the game board with the given number of rows and columns.
     * @param rows The number of rows in the game board.
     * @param cols The number of columns in the game board.
     * @param tileSize The size of each tile in the game board.
     * @return The game board as a GridPane.
     */
    private Pane setupBoard(int rows, int cols, int tileSize) {
        // Game area
        VBox borderBox = new VBox();
        borderBox.setAlignment(Pos.CENTER);
        this.add3DBorder(borderBox);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        borderBox.getChildren().add(gridPane);
        gridPane.setHgap(2);
        gridPane.setVgap(2);

        // Create a XY grid of images for the game area
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                ImageView imageView = getSpaceView(tileSize);
                gridPane.add(imageView, col, row);
                this.controller.addToBoardMap(imageView, new Pair<>(row, col));
            }
        }

        return borderBox;
    }

    /**
     * Resets the game to the initial state.
     */
    private void resetGame() {
        this.controller.setup();
    }

    /**
     * Sets the difficulty level of the game.
     * @param difficulty The difficulty level to set.
     */
    private void setDifficulty(Difficulty difficulty) {
        // Preload all images when difficulty is selected to prevent lag during gameplay
        ImagePreloader.preloadAllImages();

        this.controller.setDifficulty(difficulty);
        this.controller.clearBoardMap();
        Pane boardPane = setupBoard(difficulty.getRows(), difficulty.getCols(), difficulty.getTileSize());
        this.gameRoot.setMinWidth(difficulty.getScreenWidth());
        this.gameRoot.setMaxWidth(difficulty.getScreenWidth());
        this.gameRoot.setMinHeight(difficulty.getScreenHeight());
        this.gameRoot.setMaxHeight(difficulty.getScreenHeight());
        ((VBox) this.root.getChildren().get(0)).getChildren().set(1, boardPane);
        this.menu.setVisible(false);
        this.gameRoot.setVisible(true);
        resetGame();
    }

    /**
     * Creates an ImageView for a space on the game board.
     * @return The ImageView for the space.
     */
    private ImageView getSpaceView(int size) {
        Image image = ImagePreloader.getImage("images/minesweeper-basic/cover.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setOnMouseClicked(event -> {
            // Handle both left (PRIMARY) and right (SECONDARY) clicks
            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.MIDDLE) {
                boolean leftClick = event.getButton() == MouseButton.PRIMARY;
                this.controller.onSpaceClicked(imageView, leftClick);
            }
        });
        imageView.setOnMousePressed(event -> {
            // Handle both left and right mouse button presses
            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.MIDDLE) {
                boolean leftClick = event.getButton() == MouseButton.PRIMARY;
                this.controller.spaceClickDown(imageView, leftClick);
            }
        });
        imageView.setOnMouseReleased(event -> {
            // Handle both left and right mouse button releases
            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.MIDDLE) {
                this.controller.spaceClickUp(imageView);
            }
        });
        return imageView;
    }

    /**
     * Adds a 3D border style to the given pane.
     * @param pane The pane to style.
     */
    private void add3DBorder(Pane pane) {
        pane.setBorder(new Border(new BorderStroke(
            javafx.scene.paint.Color.rgb(136, 136, 136),
            javafx.scene.paint.Color.rgb(223, 223, 223),
            javafx.scene.paint.Color.rgb(223, 223, 223),
            javafx.scene.paint.Color.rgb(136, 136, 136),
            BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
            null, new BorderWidths(4), null)));
    }

    /**
     * Creates and adds digit ImageViews to the header.
     * @param header The header to add the digits to.
     * @param digits The array that stores the digit ImageViews.
     */
    private void makeDigits(HBox header, ImageView[] digits, boolean isLeft) {
        HBox digitsLeft = new HBox();
        // fill the width
        for (int imageNum = 0; imageNum < 3; imageNum++) {
            Image image = ImagePreloader.getImage("images/digits/0.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(28);
            imageView.setFitHeight(50);
            digitsLeft.getChildren().add(imageView);
            digits[imageNum] = imageView;
        }
        digitsLeft.setAlignment(isLeft ? javafx.geometry.Pos.BOTTOM_LEFT : javafx.geometry.Pos.BOTTOM_RIGHT);
        HBox.setHgrow(digitsLeft, javafx.scene.layout.Priority.ALWAYS);
        header.getChildren().add(digitsLeft);
    }

    /**
     * The main method to launch the application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}