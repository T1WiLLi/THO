package game.platformer.enities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Entity {

    protected float x, y;
    protected int width, height;
    protected Rectangle hitbox;

    protected int tickAnimation = 0, animationIndex = 0;

    protected int state;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void drawHitbox(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.strokeRect(hitbox.getX(), hitbox.getY(), width, height);
    }

    protected void initHitbox(float x, float y, int width, int height) {
        this.hitbox = new Rectangle(x, y, width, height);
    }

    public Rectangle getHitbox() {
        return this.hitbox;
    }

    public int getState() {
        return this.state;
    }
}
