module game.platformer {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens game.platformer to javafx.fxml;

    exports game.platformer;
    exports game.platformer.enities;
    exports game.platformer.levels;
    exports game.platformer.gamestate;
}
