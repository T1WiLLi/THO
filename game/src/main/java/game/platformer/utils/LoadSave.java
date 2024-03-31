package game.platformer.utils;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class LoadSave {

    // sprite
    public static final String PLAYER_ATLAS = "sprite/player_sprites.png";
    public static final String LEVEL_ATLAS = "sprite/outside_sprite.png";

    // Levels
    public static final String LEVEL_ONE_DATA = "leveldata/level_one_data_long.png";
    public static final String PROPS_DATA = "leveldata/level_one_props_data.png";
    public static final String BACKGROUND_LAYER_1 = "background/background_layer_1.png";
    public static final String BACKGROUND_LAYER_2 = "background/background_layer_2.png";
    public static final String BACKGROUND_LAYER_3 = "background/background_layer_3.png";

    // Props
    public static final String PROPS = "props/tree.png";

    // ui
    public static final String MENU_BUTTONS = "ui/button_atlas.png";
    public static final String MENU_BACKGROUND = "ui/menu_background.png";
    public static final String LEVEL_COMPLETE = "ui/completed_sprite.png";
    public static final String MENU_BACKGROUND_IMAGE = "ui/menuBg.png";
    public static final String OPTIONS_BACKGROUND = "ui/options_background.png";
    public static final String PAUSE_BACKGROUND = "ui/pause_menu.png";
    public static final String SOUND_BUTTON = "ui/sound_button.png";
    public static final String URM_BUTTONS = "ui/urm_buttons.png";
    public static final String VOLUME_BUTTONS = "ui/volume_buttons.png";
    public static final String DASH_BAR_SPRITE = "ui/energy_bar.png";

    // misc
    public static final String CURSOR = "misc/cursor.png";
    public static final String ICON = "misc/head-640.png";

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
