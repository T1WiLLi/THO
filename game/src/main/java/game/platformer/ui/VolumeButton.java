package game.platformer.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import static game.platformer.utils.Constants.UI.VolumeButton.*;

import game.platformer.utils.LoadSave;

public class VolumeButton extends PauseButton {

    private Image[] imgs;
    private Image slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX;
    private float floatValue = 0f;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.setX(bounds.getX() - VOLUME_WIDTH / 2);
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
        loadImgs();
    }

    private void loadImgs() {
        Image temp = LoadSave.getSprite(LoadSave.VOLUME_BUTTONS);
        imgs = new Image[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = new WritableImage(temp.getPixelReader(), i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH,
                    VOLUME_DEFAULT_HEIGHT);

        slider = new WritableImage(temp.getPixelReader(), 3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH,
                VOLUME_DEFAULT_HEIGHT);
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(slider, x, y, width, height);
        gc.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height);
    }

    public void changeX(int x) {
        if (x < minX)
            buttonX = minX;
        else if (x > maxX)
            buttonX = maxX;
        else
            buttonX = x;

        updateFloatValue();
        bounds.setX(buttonX - VOLUME_WIDTH / 2);
    }

    private void updateFloatValue() {
        float range = maxX - minX;
        float value = buttonX - minX;
        floatValue = value / range;
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

    public float getFloatValue() {
        return this.floatValue;
    }
}
