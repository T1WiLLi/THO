package game.platformer.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.scene.image.Image;

public class LoadSave {

    // sprite
    public static final String PLAYER_ATLAS = "sprite/player_sprites.png";
    public static final String LEVEL_ATLAS = "sprite/outside_sprite.png";

    // Levels
    public static final String PROPS_DATA = "leveldata/level_one_props_data.png";
    public static final String BACKGROUND_LAYER_1 = "background/background_layer_1.png";
    public static final String BACKGROUND_LAYER_2 = "background/background_layer_2.png";
    public static final String BACKGROUND_LAYER_3 = "background/background_layer_3.png";

    // Objects
    public static final String GRASS_OBJ = "sprite/grass_atlas.png";
    public static final String SPIKE_OBJ = "sprite/trap_atlas.png";
    public static final String SPIKE_BALL_OBJ = "sprite/Spiked_ball.png";
    public static final String CHAIN_OBJ = "sprite/Chain.png";

    // Props
    public static final String PROPS = "props/tree.png";

    // ui
    public static final String MENU_BUTTONS = "ui/button_atlas.png";
    public static final String MENU_BACKGROUND = "ui/menu_background.png";
    public static final String LEVEL_COMPLETE = "ui/completed_sprite.png";
    public static final String MENU_BACKGROUND_IMAGE = "ui/menuBg.png";
    public static final String DEATH_SCREEN = "ui/death_screen.png";
    public static final String CONNEXION_BACKGROUND = "ui/connexion.png";
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

    public static Image[] getAllLevels() {
        URL url = LoadSave.class.getResource("/game/platformer/assets/leveldata");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png")) {
                    filesSorted[i] = files[j];
                }
            }
        }

        Image[] imgs = new Image[filesSorted.length];
        for (int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = new Image(filesSorted[i].toURI().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imgs;
    }
}
