package game.platformer.levels;

import static game.platformer.utils.Constants.ObjectConstants.ATTACH_SPIKE_POINT;
import static game.platformer.utils.Constants.ObjectConstants.SPIKE;
import static game.platformer.utils.Constants.ObjectConstants.SPIKE_BALL;

import java.awt.Point;
import java.util.ArrayList;

import game.platformer.Game;
import game.platformer.gamestate.Playing;
import game.platformer.objects.Grass;
import game.platformer.objects.Spike;
import game.platformer.objects.SpikeBall;
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
    private ArrayList<SpikeBall> spikeBalls = new ArrayList<>();
    private ArrayList<Point> attachPoint = new ArrayList<>();

    public Level(Image image) {
        this.image = image;
        this.lvlData = new int[(int) image.getHeight()][(int) image.getWidth()];
        loadLevel();
        getSpikeBallsPoint();
        calcLvlOffsets();
    }

    private void getSpikeBallsPoint() {
        for (SpikeBall sball : this.spikeBalls) {
            sball.setClosestAttachPoint(sball.findClosestAttachPoint((int) sball.getHitbox().getX(),
                    (int) sball.getHitbox().getY(), this.attachPoint));
        }
    }

    private void loadLevel() {

        // Looping through the image colors just once. Instead of one per
        // object/enemy/etc..
        // Removed many methods in HelpMethods class.

        PixelReader pixelReader = this.image.getPixelReader();
        for (int y = 0; y < this.image.getHeight(); y++) {
            for (int x = 0; x < this.image.getWidth(); x++) {
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
            this.lvlData[y][x] = 0;
        } else {
            this.lvlData[y][x] = redValue;
        }

        switch (redValue) {
            case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 ->
                this.grass.add(new Grass((int) (x * Game.getTilesSize()),
                        (int) (y * Game.getTilesSize()) - Game.getTilesSize(),
                        getRndGrassType(x)));
        }
    }

    private int getRndGrassType(int xPos) {
        return xPos % 2;
    }

    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue) {
            case 100 -> this.playerSpawn = new Point(x * Game.getTilesSize(), y * Game.getTilesSize());
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case SPIKE -> this.spikes.add(new Spike(x * Game.getTilesSize(), y * Game.getTilesSize(), SPIKE));
            case SPIKE_BALL ->
                this.spikeBalls
                        .add(new SpikeBall(x * Game.getTilesSize(), y * Game.getTilesSize(), SPIKE_BALL));
            case ATTACH_SPIKE_POINT ->
                this.attachPoint.add(new Point(x * Game.getTilesSize(), y * Game.getTilesSize()));
        }
    }

    private void calcLvlOffsets() {
        this.lvlTilesWide = (int) this.image.getWidth();
        this.maxTilesOffset = this.lvlTilesWide - Game.getTilesInWidth();
        this.maxLvlOffsetX = Game.getTilesSize() * this.maxTilesOffset;
    }

    public int getSpriteIndex(int x, int y) {
        return this.lvlData[y][x];
    }

    public int[][] getLevelData() {
        return this.lvlData;
    }

    public int getLvlOffset() {
        return this.maxLvlOffsetX;
    }

    public Point getPlayerSpawn() {
        return this.playerSpawn;
    }

    public Image getLevelImage() {
        return this.image;
    }

    public ArrayList<Grass> getGrass() {
        return this.grass;
    }

    public ArrayList<Spike> getSpikes() {
        return this.spikes;
    }

    public ArrayList<SpikeBall> getSpikeBalls() {
        return this.spikeBalls;
    }
}
