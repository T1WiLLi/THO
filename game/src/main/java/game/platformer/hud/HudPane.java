package game.platformer.hud;

import game.platformer.Game;
import game.platformer.enities.Player;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HudPane extends Canvas {

    private GraphicsContext gc;
    private Player player;
    private DashBar dashBar;
    private Timer timer;

    private Image deathIcon;
    private int deathCount = 0;

    public HudPane(Player player) {
        super(Game.getScreenWidth(), Game.getScreenHeight());
        this.player = player;
        this.gc = getGraphicsContext2D();
        this.dashBar = new DashBar(this.player);
        this.timer = new Timer();
        this.deathIcon = LoadSave.getSprite(LoadSave.DEATH_ICON);
    }

    public void update() {
        this.timer.update();
    }

    public void render() {
        this.gc.clearRect(0, 0, Game.getScreenWidth(), Game.getScreenHeight());
        double iconCenterY = 30 + 50;
        this.gc.drawImage(deathIcon, 10, 30, 100, 100);
        this.gc.setFont(new Font(54));
        this.gc.setFill(Color.WHITESMOKE);
        this.gc.fillText(String.valueOf(this.deathCount), 120, iconCenterY + (this.gc.getFont().getSize() / 2));
        this.dashBar.render(this.gc, this.getWidth(), this.getHeight());
        this.timer.render(this.gc);
    }

    public void clearCanvas() {
        this.gc.clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    public Timer getTimer() {
        return this.timer;
    }

    public void darken() {
        this.gc.setFill(Color.rgb(0, 0, 0, 0.5));
        this.gc.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void addDeath() {
        this.deathCount++;
    }

    public void resetDeathCount() {
        this.deathCount = 0;
    }
}
