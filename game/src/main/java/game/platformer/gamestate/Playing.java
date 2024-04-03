package game.platformer.gamestate;

import game.platformer.Game;
import game.platformer.enities.Player;
import game.platformer.hud.HudPane;
import game.platformer.levels.LevelManager;
import game.platformer.objects.ObjectManager;
import game.platformer.shader.Particles;
import game.platformer.ui.Background;
import game.platformer.ui.GameOverOverlay;
import game.platformer.ui.LevelCompletedOverlay;
import game.platformer.ui.PauseOverlay;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager levelManager;
    private HudPane hud;
    private PauseOverlay pauseOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameOverOverlay gameOverOverlay;
    private Background backgroundManager;
    private ObjectManager objectManager;

    // Shader section
    private Particles particles;

    private boolean paused;
    private boolean gameOver = false;
    private boolean lvlCompleted = false;

    private int xLvlOffset;
    private int leftBorder = (int) (0.5 * Game.getScreenWidth());
    private int rightBorder = (int) (0.5 * Game.getScreenWidth());
    private int maxLvlOffsetX;

    double visibleAreaStart;
    double visibleAreaEnd;

    public Playing(Game game) {
        super(game);
        initClasses();
        calcLvlOffset();
    }

    private void initClasses() {
        this.backgroundManager = new Background(this);
        this.levelManager = new LevelManager(this, LoadSave.LEVEL_ATLAS, 48, 12, 4);
        this.objectManager = new ObjectManager(this);

        this.player = new Player(this, 100 * Game.getScale(), 200 * Game.getScale(), (int) (64 * Game.getScale()),
                (int) (40 * Game.getScale()));
        this.player.setSpawn(this.levelManager.getCurrentLevel().getPlayerSpawn());
        this.player.loadLvlData(this.levelManager.getCurrentLevel().getLevelData());

        this.hud = new HudPane(this.player);
        this.pauseOverlay = new PauseOverlay(this);
        this.gameOverOverlay = new GameOverOverlay(this);
        this.particles = new Particles();
        this.levelCompletedOverlay = new LevelCompletedOverlay(this);
        this.game.getGamePane().getChildren().addAll(this.hud, this.pauseOverlay, this.gameOverOverlay);
        this.hud.getTimer().start();
    }

    @Override
    public void update() {
        double buffer = (visibleAreaEnd - visibleAreaStart) * 0.2;
        this.visibleAreaStart = xLvlOffset - buffer;
        this.visibleAreaEnd = xLvlOffset + Game.getScreenWidth() + buffer;
        if (this.paused) {
            this.pauseOverlay.update();
        } else if (this.gameOver) {
            player.update();
            this.gameOverOverlay.update();
        } else if (this.lvlCompleted) {
            this.levelCompletedOverlay.update();
        } else {
            this.objectManager.update(this.visibleAreaStart, this.visibleAreaEnd);
            this.particles.update(xLvlOffset);
            this.levelManager.update();
            this.player.update();
            this.hud.update();
            checkCloseToBorder();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.clearRect(0, 0, this.game.getGamePane().getCanvas().getWidth(),
                this.game.getGamePane().getCanvas().getHeight());
        this.backgroundManager.render(gc, xLvlOffset);

        this.levelManager.render(gc, xLvlOffset);
        this.objectManager.render(gc, xLvlOffset, this.visibleAreaStart, this.visibleAreaEnd);
        this.particles.render(gc, xLvlOffset);
        this.hud.render();
        this.player.render(this.game.getGamePane().getGraphicsContext(), xLvlOffset);

        if (paused) {
            gc.setFill(Color.rgb(0, 0, 0, 0.5));
            this.hud.darken();
            gc.fillRect(0, 0, Game.getScreenWidth(), Game.getScreenHeight());
            this.pauseOverlay.render();
        } else if (gameOver) {
            gc.setFill(Color.rgb(255, 0, 0, 0.3));
            this.hud.darken();
            gc.fillRect(0, 0, Game.getScreenWidth(), Game.getScreenHeight());
            this.gameOverOverlay.render();
        } else if (lvlCompleted) {
            this.levelCompletedOverlay.render(gc);
        }
    }

    public void windowsFocusLost() {
        this.player.resetDirBooleans();
    }

    public void loadNextLevel() {
        resetAll();
        this.levelManager.loadNextLevel();
        this.particles.setRainRendering();
        this.player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void calcLvlOffset() {
        this.maxLvlOffsetX = this.levelManager.getCurrentLevel().getLvlOffset();
    }

    private void checkCloseToBorder() {
        int playerX = (int) this.player.getHitbox().getX();
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder) {
            xLvlOffset += diff - rightBorder;
        } else if (diff < leftBorder) {
            xLvlOffset += diff - leftBorder;
        }

        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        } else if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }
    }

    public void resetAll() {
        this.gameOver = false;
        this.paused = false;
        this.lvlCompleted = false;
        this.hud.getTimer().restart();
        this.player.resetAll();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (this.paused) {
            this.pauseOverlay.mousePressed(e);
        } else if (lvlCompleted) {
            this.levelCompletedOverlay.mousePressed(e);
        } else if (gameOver) {
            this.gameOverOverlay.mousePressed(e);
        } else {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (this.player.getDashValue() == 100) {
                    this.player.dash(0, e.getSceneX(), e.getSceneY(), xLvlOffset);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (this.paused) {
            this.pauseOverlay.mouseReleased(e);
        } else if (gameOver) {
            this.gameOverOverlay.mouseReleased(e);
        } else if (lvlCompleted) {
            this.levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (this.paused) {
            this.pauseOverlay.mouseMoved(e);
        } else if (gameOver) {
            this.gameOverOverlay.mouseMoved(e);
        } else if (lvlCompleted) {
            this.levelCompletedOverlay.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (this.paused) {
            this.pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case A:
                this.player.setLeft(true);
                break;
            case D:
                this.player.setRight(true);
                break;
            case SPACE:
                this.player.setJump(true);
                break;
            case SHIFT:
                this.player.setRunning(true);
                break;
            case TAB:
                paused = !paused;
                this.pauseOverlay.resetGraphicsContext();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getCode()) {
            case A:
                this.player.setLeft(false);
                break;
            case D:
                this.player.setRight(false);
                break;
            case SPACE:
                this.player.setJump(false);
                break;
            case SHIFT:
                this.player.setRunning(false);
                break;
            default:
                break;
        }
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
        if (this.lvlCompleted) {
            game.getAudioPlayer().lvlCompleted();
        }
    }

    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame() {
        this.paused = false;
    }

    public LevelManager getLevelManager() {
        return this.levelManager;
    }

    public ObjectManager getObjectManager() {
        return this.objectManager;
    }

    public HudPane getHudPane() {
        return this.hud;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPause(boolean pause) {
        this.paused = pause;
    }

    public boolean getPause() {
        return this.paused;
    }

    public void setPlayerDying(boolean value) {
        this.gameOver = value;
    }
}

/*
 * this.hud.clearCanvas();
 * this.hud.getTimer().stop();
 * GameState.state = GameState.MENU;
 */