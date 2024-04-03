package game.platformer.ui;

import static game.platformer.utils.Constants.UI.URMButtons.URM_SIZE;

import game.platformer.Game;
import game.platformer.gamestate.GameState;
import game.platformer.gamestate.Playing;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class GameOverOverlay extends Canvas {

    private Playing playing;
    private Image background;
    private int bgX, bgY, bgWidth, bgHeight;
    private UrmButton menu, play;

    private GraphicsContext gc;

    public GameOverOverlay(Playing playing) {
        super(Game.getScreenWidth(), Game.getScreenHeight());
        this.gc = getGraphicsContext2D();
        this.playing = playing;
        createBackground();
        createButtons();
    }

    private void createButtons() {
        int menuX = (int) (335 * Game.getScale());
        int playX = (int) (440 * Game.getScale());
        int y = (int) (195 * Game.getScale());
        this.play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);
        this.menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void createBackground() {
        this.background = LoadSave.getSprite(LoadSave.DEATH_SCREEN);
        this.bgWidth = (int) (background.getWidth() * Game.getScale());
        this.bgHeight = (int) (background.getHeight() * Game.getScale());
        this.bgX = Game.getScreenWidth() / 2 - this.bgWidth / 2;
        this.bgY = (int) (100 * Game.getScale());
    }

    public void update() {
        this.menu.update();
        this.play.update();
    }

    public void render() {
        this.gc.drawImage(this.background, bgX, bgY, bgWidth, bgHeight);
        this.menu.render(gc);
        this.play.render(gc);
    }

    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        this.play.setMouseOver(false);
        this.menu.setMouseOver(false);

        if (isIn(menu, e))
            this.menu.setMouseOver(true);
        else if (isIn(this.play, e))
            this.play.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(this.menu, e)) {
            if (this.menu.isMousePressed()) {
                this.playing.resetAll();
                this.playing.getHudPane().clearCanvas();
                this.playing.getHudPane().getTimer().stop();
                this.playing.getLightManager().resetGraphicsContext();
                this.playing.setGameState(GameState.MENU);
            }
        } else if (isIn(this.play, e))
            if (this.play.isMousePressed()) {
                this.playing.resetAll();
                this.playing.getGame().getAudioPlayer().setLevelSong(this.playing.getLevelManager().getLevelIndex());
            }

        this.menu.resetBools();
        this.play.resetBools();
        this.resetGraphicsContext();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(this.menu, e))
            this.menu.setMousePressed(true);
        else if (isIn(this.play, e))
            this.play.setMousePressed(true);
    }

    public void resetGraphicsContext() {
        this.gc.clearRect(0, 0, getWidth(), getHeight());
    }
}
