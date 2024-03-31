package game.platformer;

import game.platformer.gamestate.GameState;
import game.platformer.gamestate.Menu;
import game.platformer.gamestate.Playing;
import game.platformer.input.InputHandler;
import game.platformer.input.MouseHandler;
import game.platformer.utils.LoadSave;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.GraphicsEnvironment;

/**
 * JavaFX App
 */
public class Game extends Application implements Runnable {

    private GamePane gamePanel;
    private Scene gameScene;
    private Thread gameThread;
    private InputHandler inputHandler;
    private MouseHandler mouseHandler;

    // States
    private Playing playing;
    private Menu menu;

    // FPS
    private Label fps;
    private final int FPS_SET = retrieveMonitorFPS(); // Adapt to the screen
    private final int UPS_SET = 200;

    private final boolean SHOW_FPS_UPS = true;

    private int retrieveMonitorFPS() {
        return (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode()
                .getRefreshRate();
    }

    // Width & Height
    private final static int TILES_DEFAULT_SIZE = 32;
    private final static float SCALE = 2f; // Try to always have a round number when multi by the default size
    private final static int TILES_IN_WIDTH = 26;
    private final static int TILES_IN_HEIGHT = 14;
    private final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    private final static int SCREEN_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    private final static int SCREEN_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    @Override
    public void start(Stage gameStage) throws Exception {
        // Init enemies and player
        initClasses();
        initFPS();

        gamePanel.setOnKeyPressed(inputHandler::handle);
        gamePanel.setOnKeyReleased(inputHandler::handleKeyReleased);
        gamePanel.addEventFilter(MouseEvent.ANY, mouseHandler);

        gameScene = new Scene(gamePanel, SCREEN_WIDTH, SCREEN_HEIGHT);
        gameScene.setCursor(new ImageCursor(LoadSave.getSprite(LoadSave.CURSOR)));
        gamePanel.requestFocus();

        gameStage.setScene(gameScene);
        gameStage.setTitle("The Hooded One");
        gameStage.getIcons().add(LoadSave.getSprite(LoadSave.ICON));
        gameStage.centerOnScreen();
        gameStage.show();
        gameStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    windowsFocusLost();
                }
            }
        });
        gameStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        startGameLoop();
    }

    public void windowsFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            playing.getPlayer().resetDirBooleans();
        }
    }

    public static void startGame() {
        launch();
    }

    private void initFPS() {
        this.fps = new Label();
        this.fps.setTextFill(Color.WHITESMOKE);
        StackPane.setAlignment(fps, Pos.TOP_LEFT);
        this.gamePanel.getChildren().add(fps);
    }

    private void initClasses() {
        this.gamePanel = new GamePane(this, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.inputHandler = new InputHandler(this);
        this.mouseHandler = new MouseHandler(this);
        this.playing = new Playing(this);
        this.menu = new Menu(this, playing.getPlayer());
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void update() {
        Platform.runLater(() -> {
            switch (GameState.state) {
                case MENU -> menu.update();
                case PLAYING -> playing.update();
                // case OPTIONS -> gameOptions.update();
                case QUIT -> {
                    // Make saves here
                    Platform.exit();
                    System.exit(0);
                }
                default -> System.out.println("Error");
            }
            ;
        });
    }

    public void render() {
        Platform.runLater(() -> {
            switch (GameState.state) {
                case MENU -> menu.render(this.gamePanel.getGraphicsContext());
                case PLAYING -> playing.render(this.gamePanel.getGraphicsContext());
                // case OPTIONS -> gameOptions.render(this.gamePanel.getGraphicsContext());
                default -> System.out.println("Error");
            }
            ;
        });
    }

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

    public static float getScale() {
        return SCALE;
    }

    public static int getTilesInHeight() {
        return TILES_IN_HEIGHT;
    }

    public static int getTilesInWidth() {
        return TILES_IN_WIDTH;
    }

    public static int getTilesSize() {
        return TILES_SIZE;
    }

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public GamePane getGamePane() {
        return this.gamePanel;
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }
}