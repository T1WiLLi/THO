package game.platformer.utils;

import game.platformer.Game;
import game.platformer.enities.Player;
import game.platformer.levels.Level;
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
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.getScreenHeight())
            return true;
        float xIndex = x / Game.getTilesSize();
        float yIndex = y / Game.getTilesSize();

        return isTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    public static boolean isTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];

        switch (value) {
            case 11, 48, 49:
                return false;
            default:
                return true;
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
        if (!isSolid((float) hitbox.getX(), (float) (hitbox.getY() + hitbox.getHeight() + 1), lvlData))
            if (!isSolid((float) (hitbox.getX() + hitbox.getWidth()), (float) (hitbox.getY() + hitbox.getHeight() + 1),
                    lvlData))
                return false;
        return true;
    }

    public static boolean isFloor(Rectangle hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0)
            return isSolid((float) (hitbox.getX() + hitbox.getWidth() + xSpeed),
                    (float) (hitbox.getY() + hitbox.getHeight() + 1), lvlData);
        else
            return isSolid((float) (hitbox.getX() + xSpeed), (float) (hitbox.getY() + hitbox.getHeight() + 1), lvlData);
    }

    public static boolean isFloor(Rectangle hitbox, int[][] lvlData) {
        if (!isSolid((float) (hitbox.getX() + hitbox.getWidth()), (float) (hitbox.getY() + hitbox.getHeight() + 1),
                lvlData))
            if (!isSolid((float) hitbox.getX(), (float) (hitbox.getY() + hitbox.getHeight() + 1), lvlData))
                return false;
        return true;
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
}
