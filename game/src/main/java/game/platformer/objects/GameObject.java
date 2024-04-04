package game.platformer.objects;

import static game.platformer.utils.Constants.ANIMATION_SPEED;
import static game.platformer.utils.Constants.ObjectConstants.*;

import game.platformer.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameObject implements Renderable {
    protected int x, y, objType;
    protected Rectangle hitbox;
    protected boolean doAnimation = false;
    protected int tickAnimation = 0, animationIndex = 0;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    protected void updateAnimationTicks() {
        tickAnimation++;
        if (tickAnimation >= ANIMATION_SPEED) {
            tickAnimation = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(objType)) {
                animationIndex = 0;
            }
        }
    }

    public void reset() {
        animationIndex = 0;
        tickAnimation = 0;
        doAnimation = true;
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle(this.x, this.y, width, height);
    }

    public void drawHitbox(GraphicsContext gc, int xOffset) {
        gc.setStroke(Color.RED);
        gc.strokeRect(hitbox.getX() - xOffset, hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
    }

    public int getObjType() {
        return objType;
    }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }
}
