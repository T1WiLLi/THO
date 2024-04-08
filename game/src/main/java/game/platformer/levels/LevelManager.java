package game.platformer.levels;

import java.util.ArrayList;

import game.platformer.Game;
import game.platformer.audio.AudioPlayer;
import game.platformer.gamestate.GameState;
import game.platformer.gamestate.Playing;
import game.platformer.utils.HelpMethods;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class LevelManager {

    private Playing playing;

    private WritableImage[] sprites;
    private WritableImage[] waterSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0, aniTick, aniIndex;

    private int widthInTile;
    private int heightInTile;

    public LevelManager(Playing playing, String imageString, int AmountOfSprite, int widthInTile,
            int heightInTile) {
        this.playing = playing;
        this.widthInTile = widthInTile;
        this.heightInTile = heightInTile;
        importOutsideSprites(imageString, AmountOfSprite);
        createWater();
        this.levels = new ArrayList<>();
        buildAllLevels();
    }

    private void createWater() {
        this.waterSprite = new WritableImage[5];
        Image img = LoadSave.getSprite(LoadSave.WATER_TOP);
        for (int i = 0; i < 4; i++) {
            this.waterSprite[i] = new WritableImage(img.getPixelReader(), i * 32, 0, 32, 32);
        }

        Image bottomImg = LoadSave.getSprite(LoadSave.WATER_BOTTOM);
        int width = (int) bottomImg.getWidth();
        int height = (int) bottomImg.getHeight();
        this.waterSprite[4] = new WritableImage(bottomImg.getPixelReader(), width, height);
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
            this.playing.getGame().getAudioPlayer().stopSong();
            this.playing.setGameState(GameState.MENU);
        }

        Level newLevel = levels.get(lvlIndex);
        this.playing.getGame().getAudioPlayer().setLevelSong(AudioPlayer.LEVEL_1);
        playing.getPlayer().loadLvlData(newLevel.getLevelData());
        playing.setMaxLvlOffset(newLevel.getLvlOffset());
        playing.getObjectManager().loadObjects(newLevel);
    }

    private void buildAllLevels() {
        Image[] allLevels = LoadSave.getAllLevels();
        for (Image img : allLevels)
            this.levels.add(new Level(img));
    }

    public void update() {
        if (HelpMethods.hasPlayerFinishedLevel(getCurrentLevel())) {
            this.playing.setLevelCompleted(true);
        }
        updateWaterAnimation();
    }

    public void render(GraphicsContext gc, int xLvlOffset) {
        for (int j = 0; j < Game.getTilesInHeight(); j++) {
            for (int i = 0; i < this.levels.get(this.lvlIndex).getLevelData()[0].length; i++) {
                int index = this.levels.get(this.lvlIndex).getSpriteIndex(i, j);
                int x = Game.getTilesSize() * i - xLvlOffset;
                int y = Game.getTilesSize() * j;
                if (index == 48) {
                    gc.drawImage(this.waterSprite[aniIndex], x, y, Game.getTilesSize(), Game.getTilesSize());
                } else if (index == 49) {
                    gc.drawImage(this.waterSprite[4], x, y, Game.getTilesSize(), Game.getTilesSize());
                } else {
                    gc.drawImage(this.sprites[index], x, y, Game.getTilesSize(), Game.getTilesSize());
                }
            }
        }
    }

    private void updateWaterAnimation() {
        this.aniTick++;
        if (aniTick >= 40) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= 4) {
                aniIndex = 0;
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
