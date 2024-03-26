package game.engine;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.GraphicsEnvironment;

public class Game extends Application implements Runnable {

    private GamePane gamePanel;
    private Scene gameScene;
    private Thread gameThread;

    private final static int TILES_DEFAULT_SIZE = 16;
    private final static float SCALE = 2.3125f; // Full screen is 2.3125
    private final static int TILES_IN_WIDTH = 26; // Tiles screen sized
    private final static int TILES_IN_HEIGHT = 14; // Tiles screen sized
    private final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    private final static int SCREEN_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    private final static int SCREEN_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    // FPS
    private Label fps;

    private final int FPS_SET = retrieveMonitorFPS(); // Adapt to the screen
    private final int UPS_SET = 200;

    private final boolean SHOW_FPS_UPS = true;

    private int retrieveMonitorFPS() {
        return (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode()
                .getRefreshRate();
    }

    // Game start method
    public static void startGame() {
        launch();
    }

    // JavaFX start method
    @Override
    public void start(Stage primaryStage) throws Exception {

        initClasses();
        initFps();

        // Implements inputHandler for bot the mouse and the keyboard

        this.gameScene = new Scene(this.gamePanel);
        this.gamePanel.requestFocus();
        primaryStage.setScene(this.gameScene);
        primaryStage.setTitle("The Hooded One");
        // Set icons
        primaryStage.centerOnScreen();
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        startGameLoop();
    }

    // Thread start method
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                render();
                frames++;
                deltaF--;
            }

            if (SHOW_FPS_UPS) {
                if (System.currentTimeMillis() - lastCheck >= 1000) {
                    lastCheck = System.currentTimeMillis();
                    int finalFrames = frames;
                    int finalUpdates = updates;
                    Platform.runLater(() -> fps.setText("FPS:" + finalFrames + " | UPS: " + finalUpdates));
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;
                    updates = 0;
                }
            }
        }
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void initClasses() {
        this.gamePanel = new GamePane(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void initFps() {
        this.fps = new Label();
        fps.setTextFill(Color.BLACK);
        StackPane.setAlignment(fps, Pos.TOP_LEFT);
        gamePanel.getChildren().add(fps);
    }

    public void update() {
        Platform.runLater(() -> {
            // Update
        });
    }

    public void render() {
        Platform.runLater(() -> {
            // Render
        });
    }
}
