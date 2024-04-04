package game.platformer;

import game.platformer.audio.AudioPlayer;
import game.platformer.database.User;
import game.platformer.gamestate.GameOptions;
import game.platformer.gamestate.GameState;
import game.platformer.gamestate.Menu;
import game.platformer.gamestate.Playing;
import game.platformer.input.InputHandler;
import game.platformer.input.MouseHandler;
import game.platformer.ui.AudioOptions;
import game.platformer.utils.LoadSave;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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

    // Audio
    private AudioPlayer audioPlayer;
    private AudioOptions audioOptions;

    // User
    private User user;
    private Label username;

    // States
    private Playing playing;
    private Menu menu;
    private GameOptions gameOptions;

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
        initUser();

        gamePanel.setOnKeyPressed(inputHandler::handle);
        gamePanel.setOnKeyReleased(inputHandler::handleKeyReleased);
        gamePanel.addEventFilter(MouseEvent.ANY, mouseHandler);

        gameScene = new Scene(gamePanel, SCREEN_WIDTH, SCREEN_HEIGHT);
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
        StackPane.setAlignment(this.fps, Pos.TOP_LEFT);
        this.gamePanel.getChildren().add(this.fps);
    }

    private void initUser() {
        this.username = new Label();
        this.username.setTextFill(Color.WHITESMOKE);
        this.username.setFont(Font.font(20));
        this.username.setText("Connected as: " + this.user.getUsername());
        StackPane.setAlignment(this.username, Pos.TOP_RIGHT);
        this.gamePanel.getChildren().addAll(this.username);
    }

    private void initClasses() {
        this.user = new User(null);
        this.audioOptions = new AudioOptions(this);
        this.audioPlayer = new AudioPlayer();
        this.gameOptions = new GameOptions(this);
        this.gamePanel = new GamePane(this, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.inputHandler = new InputHandler(this);
        this.mouseHandler = new MouseHandler(this);
        this.playing = new Playing(this);
        this.menu = new Menu(this, playing.getPlayer());
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.setName("Game Thread");
        gameThread.start();
    }

    private void update() {
        Platform.runLater(() -> {
            switch (GameState.state) {
                case MENU -> this.menu.update();
                case PLAYING -> this.playing.update();
                case OPTIONS -> gameOptions.update();
                case QUIT -> {
                    Platform.exit();
                    System.exit(0);
                }
                default -> System.out.println("QUIT");
            }
            ;
            this.username.setText("Connected as: " + this.user.getUsername());
        });
    }

    public void render() {
        Platform.runLater(() -> {
            switch (GameState.state) {
                case MENU -> this.menu.render(this.gamePanel.getGraphicsContext());
                case PLAYING -> this.playing.render(this.gamePanel.getGraphicsContext());
                case OPTIONS -> gameOptions.render(this.gamePanel.getGraphicsContext());
                case QUIT -> {
                    return;
                }
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
        return this.menu;
    }

    public Playing getPlaying() {
        return this.playing;
    }

    public GameOptions getGameOptions() {
        return this.gameOptions;
    }

    public AudioOptions getAudioOptions() {
        return this.audioOptions;
    }

    public AudioPlayer getAudioPlayer() {
        return this.audioPlayer;
    }

    public User getUser() {
        return this.user;
    }
}