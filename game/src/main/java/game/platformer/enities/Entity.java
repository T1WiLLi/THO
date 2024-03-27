package game.platformer.enities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Entity {

    protected float x, y;
    protected int width, height;
    protected Rectangle hitbox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void drawHitbox(GraphicsContext gc) {
        // For debugging the hitbox
        gc.setStroke(Color.RED);
        gc.strokeRect(hitbox.getX(), hitbox.getY(), width, height);
    }

    protected void initHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle(x, y, width, height);
    }

    // protected void updateHitbox() {
    // hitbox.setX(x);
    // hitbox.setY(y);
    // }

    public Rectangle getHitbox() {
        return this.hitbox;
    }
}
