package game.platformer.utils;

import game.platformer.Game;
import javafx.scene.shape.Rectangle;

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
}
