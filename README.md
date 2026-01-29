# WebFX Minesweeper

A classic Minesweeper game built with JavaFX and compiled to web using WebFX. Play the timeless puzzle game in your browser with the same codebase running natively on desktop!

![Minesweeper Game](https://img.shields.io/badge/JavaFX-WebFX-blue)
![Build Status](https://img.shields.io/badge/build-passing-brightgreen)

## Features

- 🎮 **Three Difficulty Levels**: Beginner, Intermediate, and Expert
- 🏆 **High Score Tracking**: Save your best times for each difficulty
- 🖱️ **Classic Gameplay**: Left-click to reveal, right-click to flag
- 🌐 **Cross-Platform**: Runs in browsers (via WebFX) and as a native desktop app (via JavaFX)
- 🎨 **Dark Mode UI**: Modern dark theme with classic Minesweeper graphics
- 💾 **Local Storage**: High scores persisted using WebFX platform storage

## Game Controls

- **Left Click**: Reveal a tile - flood fill for empty spaces - clicked numbered tiles reveals adjacent tiles when flags match mine count
- **Right Click**: Place/remove a flag
- **Smiley Button**: Reset the game
- **H Key**: Toggle high scores display

## Difficulty Levels

| Level | Grid Size | Mines | Tile Size |
|-------|-----------|-------|-----------|
| Beginner | 9×9 | 10 | 32px |
| Intermediate | 16×16 | 40 | 24px |
| Expert | 16×30 | 99 | 20px |

## Technology Stack

- **JavaFX**: UI framework for desktop and web
- **WebFX**: Framework for compiling JavaFX applications to JavaScript
- **GWT**: Google Web Toolkit for web compilation
- **Maven**: Build and dependency management
- **Java 17**: Programming language

## Project Structure

```
webfx-minesweeper/
├── webfx-minesweeper-application/        # Core game logic and UI
│   └── src/main/java/minesweeper/
│       ├── MainApplication.java           # Main UI and scene setup
│       ├── Controller.java                # Game logic controller
│       ├── Board.java                     # Board state management
│       ├── Space.java                     # Individual tile logic
│       ├── SpaceItem.java                 # Tile image resources
│       ├── Difficulty.java                # Difficulty settings
│       └── HighScore.java                 # High score management
├── webfx-minesweeper-application-gwt/     # Web (GWT) build configuration
├── webfx-minesweeper-application-gluon/   # Mobile build configuration
└── webfx-minesweeper-application-openjfx/ # Desktop (OpenJFX) configuration
```

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **WebFX CLI** (optional, for WebFX-specific commands)

## Building and Running

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/webfx-minesweeper.git
cd webfx-minesweeper
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run Locally (Development Mode)

The easiest way to test the game locally is using GWT development mode with hot reload:

```bash
cd webfx-minesweeper-application-gwt
mvn gwt:devmode
```

This starts a local development server at http://localhost:9876. Open that URL in your browser, click the module link, and follow the instructions to enable dev mode.

**Troubleshooting**: If you get "Address already in use" error, kill any existing GWT processes:
```bash
pkill -f "gwt.*devmode"
```

**Note**: For a native desktop JavaFX build, you would need to copy image resources from the GWT module to the OpenJFX module, which is beyond the scope of this quick start.

### 4. Build for Web (Production)

```bash
cd webfx-minesweeper-application-gwt
mvn package -P gwt-compile
```

The compiled web application will be in:
```
webfx-minesweeper-application-gwt/target/webfx-minesweeper-application-gwt-1.0.0-SNAPSHOT/webfx_minesweeper_application_gwt/
```

### 5. Run Web Version Locally

After building for web, you can test it locally using any HTTP server:

```bash
cd webfx-minesweeper-application-gwt/target/webfx-minesweeper-application-gwt-1.0.0-SNAPSHOT/webfx_minesweeper_application_gwt/
python3 -m http.server 8000
```

Then open http://localhost:8000 in your browser.


## Development Notes

### Project Features

**Storage API**: High scores are persisted using WebFX's `dev.webfx.platform.storage` API
- **Web**: Uses browser localStorage
- **Desktop**: Uses local file system
- Format: `[playerName, timeInSeconds, difficulty]`

**Cross-Platform Images**: Images loaded with relative paths (`images/...`) work on both web and desktop

**JavaFX Best Practices**:
- All styling done via JavaFX API methods (`.setBackground()`, `.setBorder()`, etc.) instead of CSS strings
- Proper layout hierarchy using `StackPane`, `VBox`, `HBox`, and `GridPane`
- Event handling with separate `onMouseClicked`, `onMousePressed`, and `onMouseReleased` handlers

### Adding Console Logging (Optional)

If you want to add debug logging to the browser console, you'll need to:

1. Add the dependency to `webfx-minesweeper-application/pom.xml`:
```xml
<dependency>
    <groupId>dev.webfx</groupId>
    <artifactId>webfx-platform-console</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

2. Add the module requirement to `webfx-minesweeper-application/src/main/java/module-info.java`:
```java
requires webfx.platform.console;
```

3. Use in your code:
```java
import dev.webfx.platform.console.Console;

// In your methods
Console.log("Debug message: " + someVariable);
Console.warn("Warning message");
Console.error("Error message");
```

The console output will appear in your browser's developer tools (F12).

### Image Resources

All game images are located in:
```
webfx-minesweeper-application-gwt/src/main/resources/public/images/
├── minesweeper-basic/  # Game tiles (mines, flags, numbers, etc.)
└── digits/             # Seven-segment display digits
```


## Browser Compatibility

The web version has been tested on:
- ✅ Chrome/Edge (Chromium)
- ✅ Firefox
- ✅ Safari

## Known Issues

- Right-click context menu is prevented but may still appear briefly on some browsers
- Mobile touch support is limited (designed for mouse/trackpad)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

### Areas for Improvement

- [ ] Mobile/touch controls
- [ ] Sound effects
- [ ] Animations for tile reveals
- [ ] Custom themes/skins
- [ ] Multiplayer mode
- [ ] Tutorial/help screen

## License

This project is open source and available under the [MIT License](LICENSE).

## Acknowledgments

- Classic Minesweeper game mechanics from Microsoft Minesweeper
- [WebFX Framework](https://webfx.dev/) for enabling JavaFX on the web
- Tile graphics inspired by classic Windows Minesweeper
- AI generated README content using Claude

## Contact

For questions or issues, please open an issue on the GitHub repository.

---

**Enjoy playing Minesweeper! 💣**
