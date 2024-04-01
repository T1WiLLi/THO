package game.platformer.gamestate;

import static game.platformer.utils.Constants.UI.URMButtons.URM_SIZE;

import game.platformer.Game;
import game.platformer.ui.AudioOptions;
import game.platformer.ui.UrmButton;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GameOptions extends State implements StateMethods {

    private AudioOptions audioOptions;
    private Image backgroundImg, optionsBackgroundImg;
    private int bgX, bgY, bgW, bgH;

    private UrmButton menuButton;

    public GameOptions(Game game) {
        super(game);
        loadImgs();
        loadButton();
        audioOptions = game.getAudioOptions();
    }

    private void loadButton() {
        int menuX = (int) (387 * Game.getScale());
        int menuY = (int) (325 * Game.getScale());
        menuButton = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    private void loadImgs() {
        backgroundImg = LoadSave.getSprite(LoadSave.MENU_BACKGROUND_IMAGE);
        optionsBackgroundImg = LoadSave.getSprite(LoadSave.OPTIONS_BACKGROUND);

        bgW = (int) (optionsBackgroundImg.getWidth() * Game.getScale());
        bgH = (int) (optionsBackgroundImg.getHeight() * Game.getScale());
        bgX = Game.getScreenWidth() / 2 - bgW / 2;
        bgY = (int) (33 * Game.getScale());
    }

    @Override
    public void update() {
        menuButton.update();
        audioOptions.update();
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(backgroundImg, 0, 0, Game.getScreenWidth(), Game.getScreenHeight());
        gc.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH);
        menuButton.render(gc);
        audioOptions.render(gc);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                GameState.state = GameState.MENU;
            }
        } else {
            audioOptions.mouseReleased(e);
        }
        menuButton.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);
        if (isIn(e, menuButton)) {
            menuButton.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            GameState.state = GameState.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean isIn(MouseEvent e, UrmButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

}
