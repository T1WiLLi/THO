package game.platformer.hud;

import game.platformer.enities.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DashBar {

    private Player player;

    public DashBar(Player player) {
        this.player = player;
    }

    public void render(GraphicsContext gc, double screenWidth, double screenHeight) {
        double barWidth = 200d;
        double barHeight = 25d;
        float barX = (float) (screenWidth - barWidth - 50.0f);
        float barY = 10.0f;

        double filledBarWidth = (player.getDashValue() / 100) * barWidth;

        gc.setFill(Color.BLACK);
        gc.fillRect(barX, barY, barWidth, barHeight);

        gc.setFill(Color.CADETBLUE);
        gc.fillRect(barX, barY, filledBarWidth, barHeight);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        String healthLabel = "Dash Progression: " + (int) (player.getDashValue()) + " / 100";
        gc.fillText(healthLabel, barX + (barWidth / 2), (barY + (barHeight / 2)) + 3);
    }
}
