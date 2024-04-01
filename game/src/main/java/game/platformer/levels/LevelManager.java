package game.platformer.levels;

import java.util.ArrayList;

import game.platformer.Game;
import game.platformer.gamestate.GameState;
import game.platformer.gamestate.Playing;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class LevelManager {

    private Playing playing;

    private WritableImage[] sprites;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;

    private int widthInTile;
    private int heightInTile;

    public LevelManager(Playing playing, String imageString, int AmountOfSprite, int widthInTile,
            int heightInTile) {
        this.playing = playing;
        this.widthInTile = widthInTile;
        this.heightInTile = heightInTile;
        importOutsideSprites(imageString, AmountOfSprite);
        this.levels = new ArrayList<>();
        buildAllLevels();
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

    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            this.playing.setPause(false);
            this.playing.getHudPane().clearCanvas();
            this.playing.getHudPane().getTimer().stop();
            this.playing.setGameState(GameState.MENU);
        }

        Level newLevel = levels.get(lvlIndex);
        playing.getPlayer().loadLvlData(newLevel.getLevelData());
        playing.setMaxLvlOffset(newLevel.getLvlOffset());
    }

    private void buildAllLevels() {
        Image[] allLevels = LoadSave.getAllLevels();
        for (Image img : allLevels)
            this.levels.add(new Level(img));
    }

    public void render(GraphicsContext gc, int xLvlOffset) {
        for (int j = 0; j < Game.getTilesInHeight(); j++) {
            for (int i = 0; i < this.levels.get(this.lvlIndex).getLevelData()[0].length; i++) {
                int index = this.levels.get(this.lvlIndex).getSpriteIndex(i, j);
                gc.drawImage(sprites[index], Game.getTilesSize() * i - xLvlOffset, Game.getTilesSize() * j,
                        Game.getTilesSize(),
                        Game.getTilesSize());
            }
        }
    }

    public Level getCurrentLevel() {
        return this.levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }

    public int getLevelIndex() {
        return lvlIndex;
    }
}
