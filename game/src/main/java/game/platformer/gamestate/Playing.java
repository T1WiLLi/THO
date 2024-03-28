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
        this.gamePanel = game.getGamePane();
        initClasses();
    }

    private void initClasses() {
        this.levelManager = new LevelManager(game);
        this.player = new Player(200, 200, (int) (64 * Game.getScale()), (int) (40 * Game.getScale()));
        this.player.loadLvlData(this.levelManager.getCurrentLevel().getLevelData());
        this.hud = new HudPane(Game.getGameWidth(), Game.getGameHeight(), this.player);
        this.gamePanel.getChildren().addAll(this.hud);
    }

    public void windowsFocusLost() {
        this.player.resetDirBooleans();
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void update() {
        if (!this.hud.getTimer().isRunning()) {
            this.hud.getTimer().start();
        }
        this.hud.update();
        this.player.update();
    }

    @Override
    public void render(GraphicsContext gc) {
        this.hud.render();
        this.gamePanel.getCanvas().getGraphicsContext2D().clearRect(0, 0, this.gamePanel.getCanvas().getWidth(),
                this.gamePanel.getCanvas().getHeight());
        this.levelManager.render(this.gamePanel.getCanvas().getGraphicsContext2D());
        this.player.render(this.gamePanel.getGraphicsContext());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            System.out.println("Dash!");
            if (this.player.getDashValue() == 100) {
                this.player.dash(0, e.getSceneX(), e.getSceneY());
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
                this.player.setLeft(true);
                break;
            case D:
                this.player.setRight(true);
                break;
            case SPACE:
                this.player.setJump(true);
                break;
            case ESCAPE:
                this.hud.clearCanvas();
                this.hud.getTimer().stop();
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
                this.player.setLeft(false);
                break;
            case D:
                this.player.setRight(false);
                break;
            case SPACE:
                this.player.setJump(false);
                break;
            default:
                break;
        }
    }
}
