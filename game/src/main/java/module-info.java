module game.engine {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens game.engine to javafx.fxml;

    exports game.engine;
}
