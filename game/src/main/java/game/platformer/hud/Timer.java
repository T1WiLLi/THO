package game.platformer.hud;

import game.platformer.Game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Timer {

    private long startTime;
    private long stopTime;
    private long elapsedTime;
    private boolean running;

    public Timer() {
        startTime = 0;
        stopTime = 0;
        elapsedTime = 0;
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean value) {
        running = value;
    }

    public void start() {
        if (!running) {
            startTime = System.currentTimeMillis();
            running = true;
        }
    }

    public void stop() {
        if (running) {
            stopTime = System.currentTimeMillis();
            elapsedTime += stopTime - startTime;
            running = false;
        }
    }

    public void restart() {
        startTime = System.currentTimeMillis();
        stopTime = 0;
        elapsedTime = 0;
        running = true;
    }

    public void reset() {
        startTime = 0;
        stopTime = 0;
        elapsedTime = 0;
        running = false;
    }

    public void update() {
        if (running) {
            long currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - startTime;
        }
    }

    public void render(GraphicsContext gc) {
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        gc.setFill(Color.WHITE);

        long milliseconds = elapsedTime % 1000;
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = (elapsedTime / (1000 * 60)) % 60;

        String timeString = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);

        double x = Game.getGameWidth() / 2;
        double y = Game.getGameHeight() * 0.1;

        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(timeString, x, y);
        gc.setTextAlign(null);
    }
}
