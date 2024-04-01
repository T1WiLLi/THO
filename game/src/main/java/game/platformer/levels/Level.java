package game.platformer.levels;

import java.awt.Point;

import game.platformer.Game;
import game.platformer.utils.HelpMethods;
import javafx.scene.image.Image;

public class Level {

    private Image image;
    private int[][] lvlData;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(Image image) {
        this.image = image;
        createLevelData();
        calcLvlOffsets();
        calcPlayerSpawn();
    }

    private void createLevelData() {
        lvlData = HelpMethods.getLevelData(this.image);
    }

    private void calcLvlOffsets() {
        this.lvlTilesWide = (int) image.getWidth();
        this.maxTilesOffset = lvlTilesWide - Game.getTilesInWidth();
        this.maxLvlOffsetX = Game.getTilesSize() * maxTilesOffset;
    }

    private void calcPlayerSpawn() {
        playerSpawn = HelpMethods.getPlayerSpawn(this.image);
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
}
