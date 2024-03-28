package game.platformer.gamestate;

import game.platformer.Game;
import game.platformer.enities.Player;
import game.platformer.ui.MenuButtons;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Menu extends State implements StateMethods {

    Player player;
    private MenuButtons[] buttons = new MenuButtons[3];
    private Image backgroundImage, menuBg;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game, Player player) {
        super(game);
        this.player = player;
        loadButtons();
        loadBackground();
        menuBg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND_IMAGE);
    }

    private void loadBackground() {
        backgroundImage = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImage.getWidth() * Game.getScale());
        menuHeight = (int) (backgroundImage.getHeight() * Game.getScale());
        menuX = Game.getGameWidth() / 2 - menuWidth / 2;
        menuY = (int) (30 * Game.getScale());
    }

    private void loadButtons() {
        buttons[0] = new MenuButtons(Game.getGameWidth() / 2, (int) (150 * Game.getScale()), 0, GameState.PLAYING);
        buttons[1] = new MenuButtons(Game.getGameWidth() / 2, (int) (220 * Game.getScale()), 1, GameState.OPTIONS);
        buttons[2] = new MenuButtons(Game.getGameWidth() / 2, (int) (290 * Game.getScale()), 2, GameState.QUIT);
    }

    @Override
    public void update() {
        for (MenuButtons menuButton : buttons) {
            menuButton.update();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(menuBg, 0, 0, Game.getGameWidth(), Game.getGameHeight());
        gc.drawImage(backgroundImage, menuX, menuY, menuWidth, menuHeight);
        for (MenuButtons menuButton : buttons) {
            menuButton.render(gc);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButtons menuButton : buttons) {
            if (isIn(e, menuButton)) {
                menuButton.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButtons menuButton : buttons) {
            if (isIn(e, menuButton)) {
                if (menuButton.isMousePressed()) {
                    menuButton.applyGamestate();
                    break;
                }
            }
        }
        resetButtons();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    private void resetButtons() {
        for (MenuButtons menuButton : buttons) {
            menuButton.resetBool();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButtons menuButton : buttons) {
            menuButton.setMouseOver(false);
        }

        for (MenuButtons menuButton : buttons) {
            if (isIn(e, menuButton)) {
                menuButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            GameState.state = GameState.PLAYING;
            player.resetDirBooleans();
        }
    }

}
