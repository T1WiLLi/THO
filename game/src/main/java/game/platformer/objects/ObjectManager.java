package game.platformer.objects;

import static game.platformer.utils.Constants.ObjectConstants.SPIKE_HEIGHT;
import static game.platformer.utils.Constants.ObjectConstants.SPIKE_WIDTH;

import game.platformer.Game;
import game.platformer.enities.Player;
import game.platformer.gamestate.Playing;
import game.platformer.levels.Level;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class ObjectManager {

    private Playing playing;
    private WritableImage[] grassImgs;
    private Image spikeImg;

    private Level currentLevel;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        this.currentLevel = playing.getLevelManager().getCurrentLevel();
        loadImgs();
    }

    private void loadImgs() {
        this.spikeImg = LoadSave.getSprite(LoadSave.SPIKE_OBJ);
        Image grassTemp = LoadSave.getSprite(LoadSave.GRASS_OBJ);

        this.grassImgs = new WritableImage[2];
        for (int i = 0; i < this.grassImgs.length; i++) {
            this.grassImgs[i] = new WritableImage(grassTemp.getPixelReader(), 32 * i, 0, 32, 32);
        }
    }

    public void loadObjects(Level newLevel) {
        this.currentLevel = newLevel;
    }

    public void update() {
        checkSpikesTouched(this.playing.getPlayer());
    }

    private void checkSpikesTouched(Player p) {
        for (Spike s : currentLevel.getSpikes()) {
            if (s.getHitbox().intersects(p.getHitbox().getBoundsInLocal())) {
                p.kill();
            }
        }
    }

    public void render(GraphicsContext gc, int xLvlOffset) {
        renderGrass(gc, xLvlOffset);
        renderSpikes(gc, xLvlOffset);
    }

    private void renderGrass(GraphicsContext gc, int xLvlOffset) {
        for (Grass grass : currentLevel.getGrass()) {
            gc.drawImage(grassImgs[grass.getType()], grass.getX() - xLvlOffset, grass.getY(),
                    (int) (32 * Game.getScale()), (int) (32 * Game.getScale()));
        }
    }

    private void renderSpikes(GraphicsContext gc, int xLvlOffset) {
        for (Spike s : currentLevel.getSpikes()) {
            gc.drawImage(spikeImg, (int) (s.getHitbox().getX() - xLvlOffset),
                    (int) (s.getHitbox().getY() - s.getyDrawOffset()),
                    SPIKE_WIDTH, SPIKE_HEIGHT);
        }
    }
}
