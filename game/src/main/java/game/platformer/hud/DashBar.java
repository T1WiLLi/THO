package game.platformer.hud;

import game.platformer.enities.Player;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class DashBar {

    private Player player;

    public DashBar(Player player) {
        this.player = player;
    }

    public void render(GraphicsContext gc, double screenWidth, double screenHeight) {
        Image dashBar = LoadSave.getSprite(LoadSave.DASH_BAR_SPRITE);

        double barWidth = 190d;
        double barHeight = 25d;
        float barX = (float) (screenWidth - barWidth - 50.0f);
        float barY = (float) (dashBar.getHeight() / 2) + (float) barHeight / 2 - 4;

        double filledBarWidth = (player.getDashValue() / 100) * barWidth;

        double imageX = (float) (screenWidth - barWidth - 100.0f);
        double imageY = 10.0f;

        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(barX, barY, barWidth, barHeight);

        gc.setFill(Color.GOLD);
        gc.fillRect(barX, barY, filledBarWidth, barHeight);

        gc.drawImage(dashBar, imageX, imageY);
    }
}
