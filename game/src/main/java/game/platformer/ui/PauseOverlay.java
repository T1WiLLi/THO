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
import static game.platformer.utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static game.platformer.utils.Constants.UI.VolumeButton.*;

public class PauseOverlay extends Canvas {

    private Image backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB, unpauseB, restartB;
    private SoundButton musicButton, sfxButton;
    private VolumeButton volumeButton;

    private Playing playing;
    private GraphicsContext gc;

    public PauseOverlay(Playing playing) {
        super(Game.getScreenWidth(), Game.getScreenHeight());
        this.gc = getGraphicsContext2D();
        this.playing = playing;
        loadBackground();
        createURMButtons();
        createSoundButtons();
        createVolumeButton();
    }

    public void update() {
        this.menuB.update();
        this.restartB.update();
        this.unpauseB.update();
        this.musicButton.update();
        this.sfxButton.update();
        this.volumeButton.update();
    }

    public void render() {
        this.gc.clearRect(0, 0, getWidth(), getHeight());
        this.gc.drawImage(this.backgroundImg, this.bgX, this.bgY, this.bgW, this.bgH);
        this.menuB.render(this.gc);
        this.restartB.render(this.gc);
        this.unpauseB.render(this.gc);
        this.musicButton.render(this.gc);
        this.sfxButton.render(this.gc);
        this.volumeButton.render(this.gc);
    }

    private void loadBackground() {
        this.backgroundImg = LoadSave.getSprite(LoadSave.PAUSE_BACKGROUND);
        this.bgW = (int) (backgroundImg.getWidth() * Game.getScale());
        this.bgH = (int) (backgroundImg.getHeight() * Game.getScale());
        this.bgX = Game.getScreenWidth() / 2 - bgW / 2;
        this.bgY = (int) (30 * Game.getScale());
    }

    private void createURMButtons() {
        int menuX = (int) (313 * Game.getScale());
        int saveX = (int) (387 * Game.getScale());
        int unpauseX = (int) (462 * Game.getScale());
        int bY = (int) (325 * Game.getScale());

        this.menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        this.restartB = new UrmButton(saveX, bY, URM_SIZE, URM_SIZE, 1);
        this.unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    private void createSoundButtons() {
        int soundX = (int) (450 * Game.getScale());
        int musicY = (int) (145 * Game.getScale());
        int sfxY = (int) (190 * Game.getScale());
        this.musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        this.sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void createVolumeButton() {
        int vX = (int) (309 * Game.getScale());
        int vY = (int) (278 * Game.getScale());
        this.volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    public void mouseDragged(MouseEvent e) {
        if (this.volumeButton.isMousePressed()) {
            float valueBefore = this.volumeButton.getFloatValue();
            this.volumeButton.changeX((int) e.getX());
            float valueAfter = this.volumeButton.getFloatValue();
            if (valueBefore != valueAfter) {
                // Audio options later
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, this.musicButton)) {
            this.musicButton.setMousePressed(true);
        } else if (isIn(e, this.sfxButton)) {
            this.sfxButton.setMousePressed(true);
        } else if (isIn(e, this.volumeButton)) {
            this.volumeButton.setMousePressed(true);
        } else if (isIn(e, this.menuB)) {
            this.menuB.setMousePressed(true);
        } else if (isIn(e, this.unpauseB)) {
            this.unpauseB.setMousePressed(true);
        } else if (isIn(e, this.restartB)) {
            this.restartB.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, this.musicButton)) {
            if (this.musicButton.isMousePressed()) {
                this.musicButton.setMuted(!this.musicButton.isMuted());
            }
        } else if (isIn(e, this.sfxButton)) {
            if (this.sfxButton.isMousePressed()) {
                this.sfxButton.setMuted(!this.sfxButton.isMuted());
            }
        } else if (isIn(e, this.menuB)) {
            if (this.menuB.isMousePressed()) {
                this.playing.setPause(false);
                this.playing.getHudPane().clearCanvas();
                this.playing.getHudPane().getTimer().stop();
                GameState.state = GameState.MENU;
            }
        } else if (isIn(e, this.unpauseB)) {
            if (this.unpauseB.isMousePressed()) {
                this.playing.setPause(false);
            }
        } else if (isIn(e, this.restartB)) {
            if (this.restartB.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        }

        this.musicButton.resetBools();
        this.sfxButton.resetBools();
        this.volumeButton.resetBools();
        this.menuB.resetBools();
        this.restartB.resetBools();
        this.unpauseB.resetBools();
        resetGraphicsContext();
    }

    public void mouseMoved(MouseEvent e) {
        this.musicButton.setMouseOver(false);
        this.sfxButton.setMouseOver(false);
        this.volumeButton.setMouseOver(false);
        this.menuB.setMouseOver(false);
        this.restartB.setMouseOver(false);
        this.unpauseB.setMouseOver(false);

        if (isIn(e, this.musicButton)) {
            this.musicButton.setMouseOver(true);
        } else if (isIn(e, this.sfxButton)) {
            this.sfxButton.setMouseOver(true);
        } else if (isIn(e, this.volumeButton)) {
            this.volumeButton.setMouseOver(true);
        } else if (isIn(e, this.menuB)) {
            this.menuB.setMouseOver(true);
        } else if (isIn(e, this.unpauseB)) {
            this.unpauseB.setMouseOver(true);
        } else if (isIn(e, this.restartB)) {
            this.restartB.setMouseOver(true);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return (b.getBounds().contains(e.getX(), e.getY()));
    }

    public void resetGraphicsContext() {
        this.gc.clearRect(0, 0, getWidth(), getHeight());
    }
}
