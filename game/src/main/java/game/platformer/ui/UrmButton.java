package game.platformer.ui;

import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import static game.platformer.utils.Constants.UI.URMButtons.*;

public class UrmButton extends PauseButton {

    private Image[] urmImages;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    private void loadImgs() {
        Image atlas = LoadSave.getSprite(LoadSave.URM_BUTTONS);
        urmImages = new WritableImage[3];
        for (int i = 0; i < urmImages.length; i++) {
            urmImages[i] = new WritableImage(atlas.getPixelReader(), i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE,
                    URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
        }
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(urmImages[index], x, y, URM_SIZE, URM_SIZE);
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
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
}
