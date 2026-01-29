// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.minesweeper.application {

    // Direct dependencies modules
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires webfx.platform.storage;

    // Exported packages
    exports minesweeper;


    // Provided services
    provides javafx.application.Application with minesweeper.MainApplication;

}