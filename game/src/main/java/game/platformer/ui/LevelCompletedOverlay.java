package game.platformer.ui;

import game.platformer.Game;
import game.platformer.gamestate.GameState;
import game.platformer.gamestate.Playing;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import static game.platformer.utils.Constants.UI.URMButtons.URM_SIZE;

public class LevelCompletedOverlay extends Canvas {

    private Playing playing;
    private UrmButton menu, next;
    private Image background;
    private int bgX, bgY, bgWidth, bgHeight;

    private GraphicsContext gc;

    public LevelCompletedOverlay(Playing playing) {
        super(Game.getScreenWidth(), Game.getScreenHeight());
        this.gc = this.getGraphicsContext2D();
        this.playing = playing;
        initBackground();
        initButtons();
    }

    private void initBackground() {
        this.background = LoadSave.getSprite(LoadSave.LEVEL_COMPLETE);
        this.bgWidth = (int) (background.getWidth() * Game.getScale());
        this.bgHeight = (int) (background.getHeight() * Game.getScale());
        this.bgX = Game.getScreenWidth() / 2 - bgWidth / 2;
        this.bgY = (int) (30 * Game.getScale());
    }

    private void initButtons() {
        int menuX = (int) (330 * Game.getScale());
        int nextX = (int) (445 * Game.getScale());
        int y = (int) (150 * Game.getScale());
        this.next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        this.menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    public void update() {
        this.next.update();
        this.menu.update();
    }

    public void render() {
        this.gc.drawImage(this.background, this.bgX, this.bgY, this.bgWidth, this.bgHeight);
        this.next.render(this.gc);
        this.menu.render(this.gc);
    }

    public void mouseMoved(MouseEvent e) {
        this.next.setMouseOver(false);
        this.menu.setMouseOver(false);

        if (isIn(e, this.next)) {
            this.next.setMouseOver(true);
        } else if (isIn(e, this.menu)) {
            this.menu.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, this.next)) {
            if (this.next.isMousePressed()) {
                this.playing.loadNextLevel();
            }
        } else if (isIn(e, this.menu)) {
            if (this.menu.isMousePressed()) {
                this.playing.setPause(false);
                this.playing.getHudPane().clearCanvas();
                this.playing.getHudPane().getTimer().stop();
                this.playing.getLightManager().resetGraphicsContext();
                this.playing.resetAll();
                playing.setGameState(GameState.MENU);
            }
        }

        this.next.resetBools();
        this.menu.resetBools();
        this.resetGraphicsContext();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, this.next)) {
            this.next.setMousePressed(true);
        } else if (isIn(e, this.menu)) {
            this.menu.setMousePressed(true);
        }
    }

    private boolean isIn(MouseEvent e, UrmButton b) {
        return (b.getBounds().contains(e.getX(), e.getY()));
    }

    public void resetGraphicsContext() {
        this.gc.clearRect(0, 0, getWidth(), getHeight());
    }
}
