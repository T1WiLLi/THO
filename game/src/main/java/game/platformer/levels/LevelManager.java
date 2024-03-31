package game.platformer.levels;

import game.platformer.Game;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class LevelManager {

    private WritableImage[] sprites;
    private Level level;

    private int widthInTile;
    private int heightInTile;

    public LevelManager(String imageString, String dataString, int AmountOfSprite, int widthInTile, int heightInTile) {
        this.widthInTile = widthInTile;
        this.heightInTile = heightInTile;
        importOutsideSprites(imageString, AmountOfSprite);
        level = new Level(LoadSave.getLevelData(dataString));
    }

    private void importOutsideSprites(String imageString, int AmountOfSprite) {
        Image img = LoadSave.getSprite(imageString);
        sprites = new WritableImage[AmountOfSprite];

        for (int i = 0; i < heightInTile; i++) {
            for (int j = 0; j < widthInTile; j++) {
                int index = i * widthInTile + j;

                try {
                    sprites[index] = new WritableImage(img.getPixelReader(), j * 32, i * 32, 32, 32);
                } catch (ArrayIndexOutOfBoundsException e) {
                    // Ignore the exception
                }
            }
        }
    }

    public void render(GraphicsContext gc, int xLvlOffset) {
        for (int j = 0; j < Game.getTilesInHeight(); j++) {
            for (int i = 0; i < level.getLevelData()[0].length; i++) {
                int index = level.getSpriteIndex(i, j);
                gc.drawImage(sprites[index], Game.getTilesSize() * i - xLvlOffset, Game.getTilesSize() * j,
                        Game.getTilesSize(),
                        Game.getTilesSize());
            }
        }
    }

    public Level getCurrentLevel() {
        return this.level;
    }
}
