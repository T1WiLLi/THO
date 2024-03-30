package game.platformer.utils;

import java.io.InputStream;

import game.platformer.Game;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class LoadSave {

    // sprite
    public static final String PLAYER_ATLAS = "sprite/player_sprites.png";
    public static final String LEVEL_ATLAS = "sprite/outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "sprite/level_one_data_long.png";

    // ui
    public static final String MENU_BUTTONS = "ui/button_atlas.png";
    public static final String MENU_BACKGROUND = "ui/menu_background.png";
    public static final String MENU_BACKGROUND_IMAGE = "ui/menuBg.png";
    public static final String OPTIONS_BACKGROUND = "ui/options_background.png";

    // misc
    public static final String CURSOR = "misc/cursor.png";

    public static Image getSprite(String filename) {
        Image img = null;
        String path = "/game/platformer/assets/" + filename;
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

    public static int[][] getLevelData(String filename) {
        Image img = LoadSave.getSprite(filename);
        int[][] lvlData = new int[(int) img.getHeight()][(int) img.getWidth()];

        PixelReader pixelReader = img.getPixelReader();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
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
