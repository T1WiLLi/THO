package game.engine;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

public class GamePane extends StackPane {
    private Canvas canvas;
    private GraphicsContext gc;

    public GamePane(float screenWidth, float screenHeight) {
        this.canvas = new Canvas(screenWidth, screenHeight);
        this.gc = this.canvas.getGraphicsContext2D();
        getChildren().add(canvas);
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public GraphicsContext getGraphicsContext() {
        return this.gc;
    }
}
