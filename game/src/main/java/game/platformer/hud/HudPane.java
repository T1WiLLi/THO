package game.platformer.hud;

import game.platformer.Game;
import game.platformer.enities.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class HudPane extends Canvas {

    private GraphicsContext gc;
    private Player player;
    private DashBar dashBar;

    public HudPane(float screenWidth, float screenHeight, Player player) {
        setWidth(screenWidth);
        setHeight(screenHeight);
        this.player = player;
        this.gc = getGraphicsContext2D();
        this.dashBar = new DashBar(this.player);
    }

    public void update() {
        // If we ever need to make update outside of the render logic
    }

    public void render() {
        this.gc.clearRect(0, 0, Game.getGameWidth(), Game.getGameHeight());
        this.dashBar.render(this.gc, this.getWidth(), this.getHeight());
    }
}
