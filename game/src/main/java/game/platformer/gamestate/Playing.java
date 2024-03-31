package game.platformer.gamestate;

import game.platformer.Game;
import game.platformer.enities.Player;
import game.platformer.hud.HudPane;
import game.platformer.levels.LevelManager;
import game.platformer.ui.Background;
import game.platformer.ui.LevelCompletedOverlay;
import game.platformer.ui.PauseOverlay;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager levelManager;
    private HudPane hud;
    private PauseOverlay pauseOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private Background backgroundManager;

    private boolean paused;
    private boolean gameOver = false;
    private boolean lvlCompleted = true;

    private int xLvlOffset;
    private int leftBorder = (int) (0.5 * Game.getScreenWidth());
    private int rightBorder = (int) (0.5 * Game.getScreenWidth());
    private int lvlTilesWide = LoadSave.getLevelData(LoadSave.LEVEL_ONE_DATA)[0].length;
    private int maxTilesOffset = lvlTilesWide - Game.getTilesInWidth();
    private int maxLvlOffsetX = maxTilesOffset * Game.getTilesSize();

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        this.pauseOverlay = new PauseOverlay(this);
        this.levelCompletedOverlay = new LevelCompletedOverlay(this);
        this.levelManager = new LevelManager(LoadSave.LEVEL_ATLAS, LoadSave.LEVEL_ONE_DATA, 48, 12, 4);
        this.player = new Player(100 * Game.getScale(), 200 * Game.getScale(), (int) (64 * Game.getScale()),
                (int) (40 * Game.getScale()));
        this.player.loadLvlData(this.levelManager.getCurrentLevel().getLevelData());
        this.hud = new HudPane(this.player);
        this.backgroundManager = new Background();
        this.game.getGamePane().getChildren().addAll(this.hud, this.pauseOverlay);
    }

    public void windowsFocusLost() {
        this.player.resetDirBooleans();
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if (!gameOver) {
            if (!this.hud.getTimer().isRunning()) {
                this.hud.getTimer().start();
            }
            this.hud.update();
            this.player.update();
            checkCloseToBorder();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.clearRect(0, 0, this.game.getGamePane().getCanvas().getWidth(),
                this.game.getGamePane().getCanvas().getHeight());
        this.backgroundManager.render(gc, xLvlOffset);
        if (paused) {
            pauseOverlay.render();
        }
        this.hud.render();
        this.levelManager.render(this.game.getGamePane().getGraphicsContext(), xLvlOffset);
        this.player.render(this.game.getGamePane().getGraphicsContext(), xLvlOffset);

        if (lvlCompleted) {
            levelCompletedOverlay.render(gc);
        }
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
        } else if (lvlCompleted) {
            this.levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (this.paused) {
            this.pauseOverlay.mouseMoved(e);
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
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void unpauseGame() {
        this.paused = false;
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
}

/*
 * this.hud.clearCanvas();
 * this.hud.getTimer().stop();
 * GameState.state = GameState.MENU;
 */