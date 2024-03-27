package game.platformer.gamestate;

import game.platformer.Game;
import game.platformer.GamePane;
import game.platformer.enities.Player;
import game.platformer.levels.LevelManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class Playing extends State implements StateMethods {
    private Player player;
    private LevelManager levelManager;
    GamePane gp;

    public Playing(Game game) {
        super(game);
        gp = game.getGamePane();
        initClasses();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (64 * Game.getScale()), (int) (40 * Game.getScale()));
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
    }

    public void windowsFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void update() {
        levelManager.update();
        player.update();
    }

    @Override
    public void render(GraphicsContext gc) {
        gp.getCanvas().getGraphicsContext2D().clearRect(0, 0, gp.getCanvas().getWidth(),
                gp.getCanvas().getHeight());
        levelManager.draw(gp.getCanvas().getGraphicsContext2D());
        player.render(gp.getCanvas());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY)
            player.setAttack(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case A:
                player.setLeft(true);
                break;
            case D:
                player.setRight(true);
                break;
            case SPACE:
                player.setJump(true);
                break;
            case BACK_SPACE:
                GameState.state = GameState.MENU;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getCode()) {
            case A:
                player.setLeft(false);
                break;
            case D:
                player.setRight(false);
                break;
            case SPACE:
                player.setJump(false);
                break;
            default:
                break;
        }
    }
}
