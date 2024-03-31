package game.platformer.ui;

import game.platformer.Game;
import game.platformer.gamestate.GameState;
import game.platformer.gamestate.Playing;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import static game.platformer.utils.Constants.UI.URMButtons.URM_SIZE;

public class LevelCompletedOverlay {

    private Playing playing;
    private UrmButton menu, next;
    private Image background;

    private int bgX, bgY, bgWidth, bgHeight;

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initBackground();
        initButtons();
    }

    private void initBackground() {
        this.background = LoadSave.getSprite(LoadSave.LEVEL_COMPLETE);
        this.bgWidth = (int) (background.getWidth() * Game.getScale());
        this.bgHeight = (int) (background.getHeight() * Game.getScale());
        this.bgX = Game.getScreenWidth() / 2 - bgWidth / 2;
        this.bgY = (int) (75 * Game.getScale());
    }

    private void initButtons() {
        int menuX = (int) (330 * Game.getScale());
        int nextX = (int) (445 * Game.getScale());
        int y = (int) (195 * Game.getScale());
        this.next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        this.menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    public void update() {
        this.next.update();
        this.menu.update();
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(this.background, this.bgX, this.bgY, this.bgWidth, this.bgHeight);
        this.next.render(gc);
        this.menu.render(gc);
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
                this.next.setMousePressed(false);
            }
        } else if (isIn(e, this.menu)) {
            if (this.menu.isMousePressed()) {
                this.menu.setMousePressed(false);
                this.playing.setPause(false);
                this.playing.getHudPane().clearCanvas();
                this.playing.getHudPane().getTimer().stop();
                GameState.state = GameState.MENU;
            }
        }

        this.next.resetBools();
        this.menu.resetBools();
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
}