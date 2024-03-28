package game.platformer.gamestate;

import game.platformer.Game;
import game.platformer.GamePane;
import game.platformer.enities.Player;
import game.platformer.hud.HudPane;
import game.platformer.levels.LevelManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager levelManager;
    private HudPane hud;
    private GamePane gamePanel;

    public Playing(Game game) {
        super(game);
        gamePanel = game.getGamePane();
        initClasses();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (64 * Game.getScale()), (int) (40 * Game.getScale()));
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        this.hud = new HudPane(Game.getGameWidth(), Game.getGameHeight(), this.player);
        this.gamePanel.getChildren().addAll(hud);
    }

    public void windowsFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void update() {
        player.update();
    }

    @Override
    public void render(GraphicsContext gc) {
        hud.render();
        gamePanel.getCanvas().getGraphicsContext2D().clearRect(0, 0, gamePanel.getCanvas().getWidth(),
                gamePanel.getCanvas().getHeight());
        levelManager.render(gamePanel.getCanvas().getGraphicsContext2D());
        player.render(gamePanel.getCanvas());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            System.out.println("Dash!");
            if (player.getDashValue() == 100) {
                player.dash(0, e.getSceneX(), e.getSceneY());
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

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
            case ESCAPE:
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
