package game.platformer.enities;

import static game.platformer.utils.Constants.PlayerConstants.*;
import static game.platformer.utils.HelpMethods.*;

import game.platformer.utils.LoadSave;
import game.platformer.Game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class Player extends Entity {

    private int playerAction = IDLE;
    private boolean moving = false;
    private boolean left, up, down, right, jump;
    private float playerSpeed = 1.0f * Game.getScale();

    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.getScale();
    private float yDrawOffset = 4 * Game.getScale();

    // JUMP & GRAVITY
    private float airSpeed = 0f;
    private final float GRAVITY = 0.04f * Game.getScale();
    private float jumpSpeed = -2.25f * Game.getScale();
    private float fallSpeedAfterCollision = 0.8f * Game.getScale();
    private boolean inAir = false;

    // Animations logic
    private WritableImage[][] animations;
    private int tickAnimation = 0, animationIndex = 0, aniSpeed = 20;

    // Dash
    private float dash; // max 100 means we can dash, otherwise, it take 5s to recharge.
    private long lastDashTime = 0;
    private final long RECHARGE_DURATION_MS = 5000;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        this.dash = 100;
        loadAnimations();
        initHitbox(x, y, (int) (20 * Game.getScale()), (int) (27 * Game.getScale()));
    }

    public void update() {
        updatePos();
        updateDash(System.currentTimeMillis());
        updateAnimationTicks();
        setAnimation();
    }

    public void render(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(animations[playerAction][animationIndex], hitbox.getX() - xDrawOffset, hitbox.getY() - yDrawOffset,
                width, height);
    }

    private void updateDash(long currentTime) {
        long elapsedTimeSinceLastDash = currentTime - lastDashTime;
        double percentageElapsed = (double) elapsedTimeSinceLastDash / RECHARGE_DURATION_MS;
        dash = (float) Math.min(100, 100 * percentageElapsed);
    }

    private void performDash(double mouseX, double mouseY) {
        // implement the logic to do the dash, we use the left click to dash, so we want
        // to dash in that direction so we need to pass in either the mouseEvent or the
        // mouse object directly
    }

    private void updatePos() {
        moving = false;

        if (jump) {
            jump();
        }
        if (!left && !right && !inAir) {
            return;
        }

        float xSpeed = 0;

        if (left) {
            xSpeed -= playerSpeed;
        }
        if (right) {
            xSpeed += playerSpeed;
        }

        if (!inAir) {
            if (!isEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if (inAir) {
            if (canMoveHere((float) hitbox.getX(), (float) hitbox.getY() + airSpeed, (float) hitbox.getWidth(),
                    (float) hitbox.getHeight(), lvlData)) {
                hitbox.setY(hitbox.getY() + airSpeed);
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.setY(getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed));
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
        }

        moving = true;
    }

    private void jump() {
        if (inAir) {
            return;
        } else {
            inAir = true;
            airSpeed = jumpSpeed;
        }
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (canMoveHere((float) (xSpeed + hitbox.getX()), (float) (hitbox.getY()), (float) hitbox.getWidth(),
                (float) hitbox.getHeight(), lvlData)) {
            hitbox.setX(hitbox.getX() + xSpeed);
        } else {
            hitbox.setX(getEntityXPosNextToWall(hitbox, xSpeed));
        }
    }

    private void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0) {
                playerAction = JUMP;
            } else {
                playerAction = FALLING;
            }
        }

        if (startAni != playerAction) {
            tickAnimation = 0;
            animationIndex = 0;
        }
    }

    private void updateAnimationTicks() {
        tickAnimation++;
        if (tickAnimation >= aniSpeed) {
            tickAnimation = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(playerAction)) {
                animationIndex = 0;
            }
        }
    }

    private void loadAnimations() {
        Image img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new WritableImage[360 / 40][384 / 64];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = new WritableImage(img.getPixelReader(), j * 64, i * 40, 64, 40);
            }
        }
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!isEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }

    public void resetDirBooleans() {
        left = false;
        up = false;
        down = false;
        right = false;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isRight() {
        return right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isJumping() {
        return this.jump;
    }

    public void dash(float value, double mouseX, double mouseY) {
        this.dash = value;
        this.lastDashTime = System.currentTimeMillis();
        performDash(mouseX, mouseY);
    }

    public float getDashValue() {
        return this.dash;
    }
}
