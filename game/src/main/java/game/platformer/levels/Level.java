package game.platformer.levels;

import static game.platformer.utils.Constants.ObjectConstants.SPIKE;

import java.awt.Point;
import java.util.ArrayList;

import game.platformer.Game;
import game.platformer.objects.Grass;
import game.platformer.objects.Spike;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class Level {

    private Image image;
    private int[][] lvlData;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    private ArrayList<Grass> grass = new ArrayList<>();
    private ArrayList<Spike> spikes = new ArrayList<>();

    public Level(Image image) {
        this.image = image;
        lvlData = new int[(int) image.getHeight()][(int) image.getWidth()];
        loadLevel();
        calcLvlOffsets();
    }

    private void loadLevel() {

        // Looping through the image colors just once. Instead of one per
        // object/enemy/etc..
        // Removed many methods in HelpMethods class.

        PixelReader pixelReader = image.getPixelReader();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                int red = (int) (color.getRed() * 255);
                int green = (int) (color.getGreen() * 255);
                int blue = (int) (color.getBlue() * 255);

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }
        }
    }

    private void loadLevelData(int redValue, int x, int y) {
        if (redValue >= 50) {
            lvlData[y][x] = 0;
        } else {
            lvlData[y][x] = redValue;
        }

        switch (redValue) {
            case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 ->
                grass.add(new Grass((int) (x * Game.getTilesSize()),
                        (int) (y * Game.getTilesSize()) - Game.getTilesSize(),
                        getRndGrassType(x)));
        }
    }

    private int getRndGrassType(int xPos) {
        return xPos % 2;
    }

    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue) {
            case 100 -> playerSpawn = new Point(x * Game.getTilesSize(), y * Game.getTilesSize());
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case SPIKE -> spikes.add(new Spike(x * Game.getTilesSize(), y * Game.getTilesSize(), SPIKE));
        }
    }

    private void calcLvlOffsets() {
        this.lvlTilesWide = (int) image.getWidth();
        this.maxTilesOffset = lvlTilesWide - Game.getTilesInWidth();
        this.maxLvlOffsetX = Game.getTilesSize() * maxTilesOffset;
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public Image getLevelImage() {
        return this.image;
    }

    public ArrayList<Grass> getGrass() {
        return grass;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }
}
