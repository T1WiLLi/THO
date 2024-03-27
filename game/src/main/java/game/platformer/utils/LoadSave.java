package game.platformer.utils;

import java.io.InputStream;

import game.platformer.Game;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data.png";

    public static Image getSpriteAtlas(String filename) {
        Image img = null;
        String path = "/game/platformer/assets/sprite/" + filename;
        InputStream is = LoadSave.class.getResourceAsStream(path);
        try {
            img = new Image(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static int[][] getLevelData() {
        int[][] lvlData = new int[Game.getTilesInHeight()][Game.getTilesInWidth()];
        Image img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ONE_DATA);
        PixelReader pixelReader = img.getPixelReader();
        for (int j = 0; j < Game.getTilesInHeight(); j++) {
            for (int i = 0; i < Game.getTilesInWidth(); i++) {
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
