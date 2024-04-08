package game.platformer.utils;

import game.platformer.Game;
import game.platformer.levels.Level;
import game.platformer.objects.Rune;
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

    // This function is specifically for the Spike Ball
    public static boolean canMoveHereSpikeBall(double x, double y, double width, double height, double angle,
            int[][] lvlData) {
        double minX = Math.min(x, x + width * Math.cos(Math.toRadians(angle)));
        double minY = Math.min(y, y + height * Math.sin(Math.toRadians(angle)));
        double maxX = Math.max(x, x + width * Math.cos(Math.toRadians(angle)));
        double maxY = Math.max(y, y + height * Math.sin(Math.toRadians(angle)));

        if (!isSolid((float) minX, (float) minY, lvlData) &&
                !isSolid((float) maxX, (float) maxY, lvlData) &&
                !isSolid((float) maxX, (float) minY, lvlData) &&
                !isSolid((float) minX, (float) maxY, lvlData)) {
            return true;
        }
        return false;
    }

    public static boolean isSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.getTilesSize();
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.getScreenHeight())
            return true;
        float xIndex = x / Game.getTilesSize();
        float yIndex = y / Game.getTilesSize();

        return isTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    public static boolean canRainDrop(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.getTilesSize();
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.getScreenHeight())
            return true;
        float xIndex = x / Game.getTilesSize();
        float yIndex = y / Game.getTilesSize();

        int value = lvlData[(int) yIndex][(int) xIndex];

        return switch (value) {
            case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39, 48, 49, 50, 51 -> false;
            default -> true;
        };
    }

    private static boolean isTileSolid(int xTile, int yTile, int[][] lvlData) {
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

    public static boolean IsEntityInWater(Rectangle hitbox, int[][] lvlData) {
        if (GetTileValue((float) hitbox.getX(), (float) (hitbox.getY() + hitbox.getHeight()), lvlData) != 48)
            if (GetTileValue((float) (hitbox.getX() + hitbox.getWidth()), (float) (hitbox.getY() + hitbox.getHeight()),
                    lvlData) != 48)
                return false;
        return true;
    }

    private static int GetTileValue(float xPos, float yPos, int[][] lvlData) {
        int xCord = (int) (xPos / Game.getTilesSize());
        int yCord = (int) (yPos / Game.getTilesSize());
        return lvlData[yCord][xCord];
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
        if (!isSolid((float) hitbox.getX(), (float) (hitbox.getY() + hitbox.getHeight() + 1), lvlData)) {
            if (!isSolid((float) (hitbox.getX() + hitbox.getWidth()), (float) (hitbox.getY() + hitbox.getHeight() + 1),
                    lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFloor(Rectangle hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0) {
            return isSolid((float) (hitbox.getX() + hitbox.getWidth() + xSpeed),
                    (float) (hitbox.getY() + hitbox.getHeight() + 1), lvlData);
        } else {
            return isSolid((float) (hitbox.getX() + xSpeed), (float) (hitbox.getY() + hitbox.getHeight() + 1), lvlData);
        }
    }

    public static boolean isFloor(Rectangle hitbox, int[][] lvlData) {
        if (!isSolid((float) (hitbox.getX() + hitbox.getWidth()), (float) (hitbox.getY() + hitbox.getHeight() + 1),
                lvlData))
            if (!isSolid((float) hitbox.getX(), (float) (hitbox.getY() + hitbox.getHeight() + 1), lvlData))
                return false;
        return true;
    }

    public static boolean hasPlayerFinishedLevel(Level level) {
        for (Rune rune : level.getRunes()) {
            if (rune.hasPlayerPassedTheLevel()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
