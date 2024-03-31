package game.platformer.ui;

import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import static game.platformer.utils.Constants.UI.PauseButtons.SOUND_SIZE_DEFAULT;

public class SoundButton extends PauseButton {

    private WritableImage[][] soundImages;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, columnIndex;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadImgs();
    }

    private void loadImgs() {
        Image atlas = LoadSave.getSprite(LoadSave.SOUND_BUTTON);
        soundImages = new WritableImage[2][3];
        for (int i = 0; i < soundImages.length; i++) {
            for (int j = 0; j < soundImages[i].length; j++) {
                soundImages[i][j] = new WritableImage(atlas.getPixelReader(), j * SOUND_SIZE_DEFAULT,
                        i * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
            }
        }
    }

    public void update() {
        if (muted) {
            rowIndex = 1;
        } else {
            rowIndex = 0;
        }
        columnIndex = 0;
        if (mouseOver)
            columnIndex = 1;
        if (mousePressed)
            columnIndex = 2;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(soundImages[rowIndex][columnIndex], x, y, width, height);
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

}
