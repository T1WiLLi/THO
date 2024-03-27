package game.platformer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

public class GamePane extends StackPane {

    private Canvas canvas;
    private GraphicsContext gc;

    public GamePane(double width, double height) {
        this.canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        getChildren().add(this.canvas);
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public GraphicsContext getGraphicsContext() {
        return this.gc;
    }
}
