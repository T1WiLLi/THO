package game.platformer.ui;

import game.platformer.gamestate.GameState;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;

import static game.platformer.utils.Constants.UI.Buttons.*;

public class MenuButtons {

    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTH / 2;
    private GameState state;
    private WritableImage[] imgs;
    private Rectangle bounds;

    private boolean mouseOver, mousePressed;

    public MenuButtons(int xPos, int yPos, int rowIndex, GameState state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImgs() {
        imgs = new WritableImage[3];
        Image temp = LoadSave.getSprite(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = new WritableImage(temp.getPixelReader(), i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT,
                    B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
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

    public Rectangle getBounds() {
        return bounds;
    }

    public void applyGamestate() {
        GameState.state = state;
    }

    public void resetBool() {
        mouseOver = false;
        mousePressed = false;
    }

    public GameState getState() {
        return state;
    }

}
