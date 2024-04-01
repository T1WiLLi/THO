package game.platformer.ui;

import game.platformer.Game;
import game.platformer.gamestate.Playing;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Background {

    private Playing playing;

    private Image backgroundLayerOne;
    private Image backgroundLayerTwo;
    private Image backgroundLayerThree;

    private final int LAYER_WIDTH_DEFAULT = 320;
    private final int LAYER_WIDTH = (int) (this.LAYER_WIDTH_DEFAULT * Game.getScale());

    public Background(Playing playing) {
        this.playing = playing;
        this.backgroundLayerOne = LoadSave.getSprite(LoadSave.BACKGROUND_LAYER_1);
        this.backgroundLayerTwo = LoadSave.getSprite(LoadSave.BACKGROUND_LAYER_2);
        this.backgroundLayerThree = LoadSave.getSprite(LoadSave.BACKGROUND_LAYER_3);
    }

    public void render(GraphicsContext gc, int xLvlOffset) {
        gc.setFill(Color.rgb(25, 25, 25));
        gc.fillRect(0, 0, Game.getScreenWidth(), Game.getScreenHeight());

        int widthOfLevel = (int) (playing.getLevelManager().getCurrentLevel().getLevelImage().getWidth()
                * Game.getTilesSize());
        int amountOfLayerToBeDrawn = (int) (widthOfLevel / this.LAYER_WIDTH);

        for (int i = 0; i < amountOfLayerToBeDrawn; i++) {
            gc.drawImage(this.backgroundLayerOne, i * LAYER_WIDTH - (int) (xLvlOffset * 0.1),
                    (Game.getTilesSize() * 3), LAYER_WIDTH,
                    Game.getScreenHeight() - (Game.getTilesSize() * 7));
        }
        for (int i = 0; i < amountOfLayerToBeDrawn; i++) {
            gc.drawImage(this.backgroundLayerTwo, i * LAYER_WIDTH - (int) (xLvlOffset * 0.25),
                    (Game.getTilesSize() * 3), LAYER_WIDTH,
                    Game.getScreenHeight() - (Game.getTilesSize() * 7));
        }
        for (int i = 0; i < amountOfLayerToBeDrawn; i++) {
            gc.drawImage(this.backgroundLayerThree, i * LAYER_WIDTH - (int) (xLvlOffset * 0.5),
                    (Game.getTilesSize() * 3), LAYER_WIDTH,
                    Game.getScreenHeight() - (Game.getTilesSize() * 7));
        }
    }
}
