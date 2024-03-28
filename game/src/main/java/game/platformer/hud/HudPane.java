package game.platformer.hud;

import game.platformer.Game;
import game.platformer.enities.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class HudPane extends Canvas {

    private GraphicsContext gc;
    private Player player;
    private DashBar dashBar;
    private Timer timer;

    public HudPane(float screenWidth, float screenHeight, Player player) {
        setWidth(screenWidth);
        setHeight(screenHeight);
        this.player = player;
        this.gc = getGraphicsContext2D();
        this.dashBar = new DashBar(this.player);
        this.timer = new Timer();
    }

    public void update() {
        this.timer.update();
    }

    public void render() {
        this.gc.clearRect(0, 0, Game.getGameWidth(), Game.getGameHeight());
        this.dashBar.render(this.gc, this.getWidth(), this.getHeight());
        this.timer.render(this.gc);
    }

    public void clearCanvas() {
        this.gc.clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    public Timer getTimer() {
        return this.timer;
    }
}
