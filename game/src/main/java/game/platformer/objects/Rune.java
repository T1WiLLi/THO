package game.platformer.objects;

import game.platformer.Game;
import game.platformer.enities.Player;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import static game.platformer.utils.Constants.ObjectConstants.RUNE_WIDTH;
import static game.platformer.utils.Constants.ObjectConstants.RUNE_WIDTH_DEFAULT;
import static game.platformer.utils.Constants.ObjectConstants.RUNE_HEIGHT;
import static game.platformer.utils.Constants.ObjectConstants.RUNE_HEIGHT_DEFAULT;

public class Rune extends GameObject {

    private int tileX, tileY;
    private WritableImage[] sprites;

    private boolean hasPassed = false;

    public Rune(int x, int y, int objType) {
        super(x, y, objType);
        this.tileX = this.x / Game.getTilesSize();
        this.tileY = this.y / Game.getTilesSize();
        initHitbox(RUNE_WIDTH, RUNE_HEIGHT);
        loadAnimations();
        this.doAnimation = false;
    }

    private void loadAnimations() {
        Image img = LoadSave.getSprite(LoadSave.RUNE_OBJ);
        this.sprites = new WritableImage[11]; // But 1 and 11, are for inactive and active runes
        for (int i = 1; i < 10; i++) {
            this.sprites[i] = new WritableImage(img.getPixelReader(), i * 32, 0, RUNE_WIDTH_DEFAULT,
                    RUNE_HEIGHT_DEFAULT);
        }
        // Load specific
        this.sprites[0] = new WritableImage(img.getPixelReader(), 0, 0, RUNE_WIDTH_DEFAULT, RUNE_HEIGHT_DEFAULT);
        this.sprites[10] = new WritableImage(img.getPixelReader(), 10 * 32, 0, RUNE_WIDTH_DEFAULT, RUNE_HEIGHT_DEFAULT);
    }

    public void update(Player player) {
        if (this.doAnimation) {
            updateAnimationTicks();
        }
        if (this.animationIndex == this.sprites.length - 1) {
            this.doAnimation = false;
        }
        if (this.tileX == (int) player.getHitbox().getX() / Game.getTilesSize()
                && this.tileY == (int) player.getHitbox().getY() / Game.getTilesSize()) {
            this.doAnimation = true;
        }
    }

    public void render(GraphicsContext gc, int xLvlOffset) {
        if (hasPassed) {
            gc.drawImage(this.sprites[this.sprites.length - 1], this.x - xLvlOffset, this.y, Game.getTilesSize(),
                    Game.getTilesSize());
        } else {
            gc.drawImage(this.sprites[animationIndex], this.x - xLvlOffset, this.y, Game.getTilesSize(),
                    Game.getTilesSize());
        }
    }

    public int[] getTileXY() {
        return new int[] { (int) this.hitbox.getX(), (int) this.hitbox.getY() };
    }

    public boolean hasPlayerPassedTheLevel() {
        if (this.animationIndex == this.sprites.length - 1) {
            this.hasPassed = true;
            return true;
        }
        return false;
    }

    public void reset() {
        this.doAnimation = false;
        this.hasPassed = false;
        this.animationIndex = 0;
        this.tickAnimation = 0;
    }
}
