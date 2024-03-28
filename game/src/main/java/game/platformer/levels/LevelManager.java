package game.platformer.levels;

import game.platformer.Game;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class LevelManager {

    private WritableImage[] levelSprite;
    private Level levelOne;

    public LevelManager(Game game) {
        importOutsideSprites();
        levelOne = new Level(LoadSave.getLevelData(LoadSave.LEVEL_ONE_DATA));
    }

    private void importOutsideSprites() {
        Image img = LoadSave.getSprite(LoadSave.LEVEL_ATLAS);
        levelSprite = new WritableImage[48];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                int index = i * 12 + j;

                levelSprite[index] = new WritableImage(img.getPixelReader(), j * 32, i * 32, 32, 32);
            }
        }
    }

    public void render(GraphicsContext gc) {
        for (int i = 0; i < Game.getTilesInHeight(); i++) {
            for (int j = 0; j < Game.getTilesInWidth(); j++) {
                int index = levelOne.getSpriteIndex(j, i);
                gc.drawImage(levelSprite[index], Game.getTilesSize() * j, Game.getTilesSize() * i, Game.getTilesSize(),
                        Game.getTilesSize());
            }
        }
    }

    public Level getCurrentLevel() {
        return this.levelOne;
    }
}
