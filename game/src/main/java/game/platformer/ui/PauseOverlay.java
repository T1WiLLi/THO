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

public class PauseOverlay extends Canvas {

    private Image backgroundImg;
    private int bgX, bgY, bgW, bgH;

    private UrmButton menuB, unpauseB, saveB;

    private Playing playing;
    private AudioOptions audioOptions;

    private GraphicsContext gc;

    public PauseOverlay(Playing playing) {
        super(Game.getScreenWidth(), Game.getScreenHeight());
        this.gc = getGraphicsContext2D();
        this.playing = playing;
        this.audioOptions = playing.getGame().getAudioOptions();
        loadBackground();
        createURMButtons();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.getSprite(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.getScale());
        bgH = (int) (backgroundImg.getHeight() * Game.getScale());
        bgX = Game.getScreenWidth() / 2 - bgW / 2;
        bgY = (int) (30 * Game.getScale());
    }

    private void createURMButtons() {
        int menuX = (int) (313 * Game.getScale());
        int saveX = (int) (387 * Game.getScale());
        int unpauseX = (int) (462 * Game.getScale());
        int bY = (int) (325 * Game.getScale());

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        saveB = new UrmButton(saveX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    public void update() {
        menuB.update();
        saveB.update();
        unpauseB.update();
        audioOptions.update();
    }

    public void render() {
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(backgroundImg, bgX, bgY, bgW, bgH);
        menuB.render(gc);
        saveB.render(gc);
        unpauseB.render(gc);
        audioOptions.render(gc);
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        } else if (isIn(e, saveB)) {
            saveB.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                playing.setPause(false);
                playing.getHudPane().clearCanvas();
                playing.getLightManager().resetGraphicsContext();
                playing.setGameState(GameState.MENU);
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()) {
                playing.setPause(false);
            }
        } else if (isIn(e, saveB)) {
            if (saveB.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        } else {
            audioOptions.mouseReleased(e);
        }

        menuB.resetBools();
        saveB.resetBools();
        unpauseB.resetBools();
        resetGraphicsContext();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        saveB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        } else if (isIn(e, saveB)) {
            saveB.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return (b.getBounds().contains(e.getX(), e.getY()));
    }

    public void resetGraphicsContext() {
        this.gc.clearRect(0, 0, getWidth(), getHeight());
    }
}
