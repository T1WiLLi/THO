package game.platformer.utils;

import game.platformer.Game;
import game.platformer.enities.Player;
import game.platformer.levels.Level;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.Point;

public class HelpMethods {

    public static boolean canMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!isSolid(x, y, lvlData)) {
            if (!isSolid(x + width, y + height, lvlData)) {
                if (!isSolid(x + width, y, lvlData)) {
                    if (!isSolid(x, y + height, lvlData)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.getTilesSize();
        if (x < 0 || x >= maxWidth) {
            return true;
        }
        if (y < 0 || y >= Game.getScreenHeight()) {
            return true;
        }

        float xIndex = x / Game.getTilesSize();
        float yIndex = y / Game.getTilesSize();

        int value = lvlData[(int) yIndex][(int) xIndex];

        if (value >= 48 || value < 0 || value != 11) {
            return true;
        } else {
            return false;
        }
    }

    public static float getEntityXPosNextToWall(Rectangle hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.getX()) / Game.getTilesSize();
        if (xSpeed > 0) {
            int tileXPos = currentTile * Game.getTilesSize();
            int xOffset = (int) (Game.getTilesSize() - hitbox.getWidth());
            return (xOffset - 1) + tileXPos;
        } else {
            return currentTile * Game.getTilesSize();
        }
    }

    public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.getY()) / Game.getTilesSize();
        if (airSpeed > 0) {
            int tileYPos = currentTile * Game.getTilesSize();
            int yOffset = (int) (Game.getTilesSize() - hitbox.getHeight());
            return (yOffset - 1) + tileYPos;
        } else {
            return currentTile * Game.getTilesSize();
        }
    }

    public static boolean isEntityOnFloor(Rectangle hitbox, int[][] lvlData) {
        if (!isSolid((float) hitbox.getX(), (float) hitbox.getY() + (float) hitbox.getHeight() + 1, lvlData)) {
            if (!isSolid((float) hitbox.getX() + (float) hitbox.getWidth(),
                    (float) hitbox.getY() + (float) hitbox.getHeight() + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static Point getPlayerSpawn(Image img) {
        PixelReader pixelReader = img.getPixelReader();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = pixelReader.getColor(i, j);
                int value = (int) (color.getGreen() * 255);
                if (value == 100)
                    return new Point(i * Game.getTilesSize(), j * Game.getTilesSize());
            }
        return new Point(1 * Game.getTilesSize(), 1 * Game.getTilesSize());
    }

    public static boolean hasPlayerFinishedLevel(Level level, Player player) {
        int endOfLevel = level.getLevelData()[0].length * Game.getTilesSize(); // Get Width IN PX
        Point spawnPoint = player.getSpawnPoint();

        if (spawnPoint.getX() >= endOfLevel / 2) {
            return player.getHitbox().getX() <= 100;
        } else {
            return player.getHitbox().getX() >= endOfLevel - 100;
        }
    }

    public static int[][] getLevelData(Image image) {
        int[][] lvlData = new int[(int) image.getHeight()][(int) image.getWidth()];
        PixelReader pixelReader = image.getPixelReader();
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = pixelReader.getColor(i, j);
                int value = (int) (color.getRed() * 255);
                if (value >= 48) {
                    value = 0;
                }
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }
}
