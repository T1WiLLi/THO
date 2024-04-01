package game.platformer.gamestate;

import game.platformer.Game;
import game.platformer.audio.AudioPlayer;
import game.platformer.database.Connexion;
import game.platformer.enities.Player;
import game.platformer.ui.MenuButtons;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Menu extends State implements StateMethods {

    Player player;
    private Connexion connexion;
    private MenuButtons[] buttons = new MenuButtons[3];
    private Image backgroundImage, menuBg;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game, Player player) {
        super(game);
        this.player = player;
        loadButtons();
        loadBackground();
        this.connexion = new Connexion(this, game.getGamePane());
        this.menuBg = LoadSave.getSprite(LoadSave.MENU_BACKGROUND_IMAGE);
    }

    private void loadBackground() {
        this.backgroundImage = LoadSave.getSprite(LoadSave.MENU_BACKGROUND);
        this.menuWidth = (int) (backgroundImage.getWidth() * Game.getScale());
        this.menuHeight = (int) (backgroundImage.getHeight() * Game.getScale());
        this.menuX = Game.getScreenWidth() / 2 - menuWidth / 2;
        this.menuY = (int) (30 * Game.getScale());
    }

    private void loadButtons() {
        this.buttons[0] = new MenuButtons(Game.getScreenWidth() / 2, (int) (150 * Game.getScale()), 0,
                GameState.PLAYING);
        this.buttons[1] = new MenuButtons(Game.getScreenWidth() / 2, (int) (220 * Game.getScale()), 1,
                GameState.OPTIONS);
        this.buttons[2] = new MenuButtons(Game.getScreenWidth() / 2, (int) (290 * Game.getScale()), 2, GameState.QUIT);
    }

    @Override
    public void update() {
        this.connexion.update();
        for (MenuButtons menuButton : this.buttons) {
            menuButton.update();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.clearRect(0, 0, this.game.getGamePane().getCanvas().getWidth(),
                this.game.getGamePane().getCanvas().getHeight());
        gc.drawImage(menuBg, 0, 0, Game.getScreenWidth(), Game.getScreenHeight());
        gc.drawImage(this.backgroundImage, this.menuX, this.menuY, this.menuWidth, this.menuHeight);
        for (MenuButtons menuButton : this.buttons) {
            menuButton.render(gc);
        }
        this.connexion.render(gc);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButtons menuButton : this.buttons) {
            if (isIn(e, menuButton)) {
                menuButton.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButtons menuButton : this.buttons) {
            if (isIn(e, menuButton)) {
                if (menuButton.isMousePressed()) {
                    menuButton.applyGamestate();
                    if (menuButton.getState() == GameState.PLAYING) {
                        game.getPlaying().resetAll();
                        this.connexion.hidePane();
                        game.getAudioPlayer().setLevelSong(AudioPlayer.LEVEL_1);
                    } else if (menuButton.getState() == GameState.OPTIONS) {
                        this.connexion.hidePane();
                    }
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
        for (MenuButtons menuButton : this.buttons) {
            menuButton.resetBool();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButtons menuButton : this.buttons) {
            menuButton.setMouseOver(false);
        }

        for (MenuButtons menuButton : this.buttons) {
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

    }
}
