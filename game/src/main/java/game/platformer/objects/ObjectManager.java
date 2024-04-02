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

    private void rotateSpikeBalls(int[][] lvlData) {
        for (SpikeBall sBall : this.currentLevel.getSpikeBalls()) {
            sBall.update(lvlData);
        }
    }

    private void checkSpikesBallTouched(Player p) {
        for (SpikeBall sBall : this.currentLevel.getSpikeBalls()) {
            if (sBall.getHitbox().intersects(p.getHitbox().getBoundsInLocal())) {
                p.kill();
            }
        }
    }

    private void checkSpikesTouched(Player p) {
        for (Spike s : this.currentLevel.getSpikes()) {
            if (s.getHitbox().intersects(p.getHitbox().getBoundsInLocal())) {
                p.kill();
            }
        }
    }

    public void update(double visibleAreaStart, double visibleAreaEnd) {
        Player player = playing.getPlayer();
        checkSpikesTouched(player);
        checkSpikesBallTouched(player);

        for (SpikeBall sBall : currentLevel.getSpikeBalls()) {
            if (isInVisibleArea(sBall, visibleAreaStart, visibleAreaEnd)) {
                rotateSpikeBalls(this.currentLevel.getLevelData());
            }
        }
    }

    public void render(GraphicsContext gc, double xLvlOffset, double visibleAreaStart, double visibleAreaEnd) {
        renderGrass(gc, xLvlOffset, visibleAreaStart, visibleAreaEnd);
        renderSpikes(gc, xLvlOffset, visibleAreaStart, visibleAreaEnd);
        renderSpikesBall(gc, xLvlOffset, visibleAreaStart, visibleAreaEnd);
    }

    private void renderGrass(GraphicsContext gc, double xLvlOffset, double visibleAreaStart, double visibleAreaEnd) {
        for (Grass grass : currentLevel.getGrass()) {
            if (isInVisibleArea(grass, visibleAreaStart, visibleAreaEnd))
                gc.drawImage(grassImgs[grass.getType()], grass.getX() - xLvlOffset, grass.getY(),
                        (int) (32 * Game.getScale()), (int) (32 * Game.getScale()));
        }
    }

    private void renderSpikes(GraphicsContext gc, double xLvlOffset, double visibleAreaStart, double visibleAreaEnd) {
        for (Spike s : currentLevel.getSpikes()) {
            if (isInVisibleArea(s, visibleAreaStart, visibleAreaEnd)) {
                gc.drawImage(spikeImg, (int) (s.getHitbox().getX() - xLvlOffset),
                        (int) (s.getHitbox().getY() - s.getyDrawOffset()),
                        SPIKE_WIDTH, SPIKE_HEIGHT);
            }
        }
    }

    private void renderSpikesBall(GraphicsContext gc, double xLvlOffset, double visibleAreaStart,
            double visibleAreaEnd) {
        for (SpikeBall sball : currentLevel.getSpikeBalls()) {
            if (isInVisibleArea(sball, visibleAreaStart, visibleAreaEnd)) {
                sball.render(gc, (int) xLvlOffset);
            }
        }
    }

    private boolean isInVisibleArea(GameObject gameObject, double visibleAreaStart, double visibleAreaEnd) {
        double gameObjectX = gameObject.getHitbox().getX();
        double gameObjectWidth = gameObject.getHitbox().getWidth();

        return (gameObjectX + gameObjectWidth >= visibleAreaStart) && (gameObjectX <= visibleAreaEnd);
    }
}
