package game.platformer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

public class GamePane extends StackPane {

    private Game game;
    private Canvas canvas;
    private GraphicsContext gc;

    public GamePane(Game game, double width, double height) {
        this.game = game;
        this.canvas = new ResizableCanvas();
        gc = canvas.getGraphicsContext2D();
        getChildren().add(this.canvas);

        this.canvas.widthProperty().bind(this.widthProperty());
        this.canvas.heightProperty().bind(this.heightProperty());
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public GraphicsContext getGraphicsContext() {
        return this.gc;
    }

    private class ResizableCanvas extends Canvas {
        public ResizableCanvas() {
            this.widthProperty().addListener(evt -> game.render());
            this.heightProperty().addListener(evt -> game.render());
        }

        @Override
        public boolean isResizable() {
            return true;
        }

        @Override
        public double prefWidth(double height) {
            return getWidth();
        }

        @Override
        public double prefHeight(double width) {
            return getHeight();
        }
    }
}
