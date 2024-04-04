package game.platformer.enities;

import static game.platformer.utils.Constants.PlayerConstants.*;
import static game.platformer.utils.Constants.GRAVITY;
import static game.platformer.utils.Constants.ANIMATION_SPEED;
import static game.platformer.utils.HelpMethods.*;

import java.util.Timer;
import java.util.TimerTask;

import game.platformer.utils.LoadSave;
import game.platformer.Game;
import game.platformer.audio.AudioPlayer;
import game.platformer.gamestate.Playing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.awt.Point;

public class Player extends Entity {

    private Playing playing;

    private boolean moving = false;
    private boolean left, right, jump, facingRight = true, isAlive = false;
    private float playerSpeed = 0.75f * Game.getScale();
    private Point spawPoint;

    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.getScale();
    private float yDrawOffset = 4 * Game.getScale();

    // JUMP & GRAVITY
    private float airSpeed = 0f;
    private float jumpSpeed = -2.25f * Game.getScale();
    private float fallSpeedAfterCollision = 0.8f * Game.getScale();
    private boolean inAir = false;

    // Animations logic
    private WritableImage[][] animations;

    // Dash
    private float dash; // max 100 means we can dash, otherwise, it take 500ms to recharge.
    private long lastDashTime = 0;
    private final long RECHARGE_DURATION_MS = 500;

    public Player(Playing playing, float x, float y, int width, int height) {
        super(x, y, width, height);
        this.playing = playing;
        this.dash = 100;
        this.state = IDLE;
        loadAnimations();
        initHitbox(x, y, (int) (20 * Game.getScale()), (int) (27 * Game.getScale()));
    }

    public void setSpawn(Point spawn) {
        this.spawPoint = spawn;
        this.x = (float) spawn.getX();
        this.y = (float) spawn.getY();
        hitbox.setX(this.x);
        hitbox.setY(this.y);
    }

    public void update() {
        if (isAlive) {
            updatePos();
            updateDash(System.currentTimeMillis());
            updateAnimationTicks();
            setAnimation();
            checkInsideWater();
        } else {
            playing.setPlayerDying(true);
        }
    }

    public void render(GraphicsContext gc, int xLvlOffset) {
        if (!facingRight) {
            gc.save();
            gc.scale(-1, 1);
        }
        gc.drawImage(animations[state][animationIndex],
                ((facingRight) ? (int) (hitbox.getX() - xDrawOffset) - xLvlOffset
                        : -(hitbox.getX() - xDrawOffset - xLvlOffset) - width),
                (int) (hitbox.getY() - yDrawOffset), width, height);
        if (!facingRight) {
            gc.restore();
        }
    }

    private void checkInsideWater() {
        if (IsEntityInWater(hitbox, playing.getLevelManager().getCurrentLevel().getLevelData()))
            this.kill();
    }

    private void updateDash(long currentTime) {
        long elapsedTimeSinceLastDash = currentTime - lastDashTime;
        double percentageElapsed = (double) elapsedTimeSinceLastDash / RECHARGE_DURATION_MS;
        dash = (float) Math.min(100, 100 * percentageElapsed);
    }

    private void performDash(double mouseX, double mouseY, int xLvlOffset) {
        float originalSpeed = this.playerSpeed;
        float originalJumpSpeed = this.jumpSpeed;
        double deltaX = (mouseX + xLvlOffset) - hitbox.getX();
        double deltaY = mouseY - hitbox.getY();
        double distanceToMouse = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (deltaX > (hitbox.getX() - (mouseX + xLvlOffset))) {
            facingRight = true;
        } else {
            facingRight = false;
        }

        float dirX = (float) (deltaX / distanceToMouse);
        float dirY = (float) (deltaY / distanceToMouse);

        this.playerSpeed *= 5;
        this.jumpSpeed *= 5;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                playerSpeed = originalSpeed;
                jumpSpeed = originalJumpSpeed;
                timer.cancel();
            }
        }, 50);

        TimerTask dashTask = new TimerTask() {
            @Override
            public void run() {
                if (canMoveHere((float) (hitbox.getX() + dirX * playerSpeed),
                        (float) (hitbox.getY() + dirY * playerSpeed),
                        (float) hitbox.getWidth(), (float) hitbox.getHeight(), lvlData)) {
                    hitbox.setX(hitbox.getX() + dirX * playerSpeed);
                    hitbox.setY(hitbox.getY() + dirY * playerSpeed);
                }

                if (!isEntityOnFloor(hitbox, lvlData)) {
                    inAir = true;
                }

                float speedDecrement = (originalSpeed - playerSpeed) / 50f;
                playerSpeed += speedDecrement;
            }
        };

        long period = (long) (1000.0 / 200);
        timer.scheduleAtFixedRate(dashTask, 0, period);
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
            facingRight = false;
        }
        if (right) {
            xSpeed += playerSpeed;
            facingRight = true;
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
        if (!inAir) {
            inAir = true;
            airSpeed = jumpSpeed;
            this.playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        }
    }

    public void kill() {
        this.playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
        this.playing.getGame().getAudioPlayer().stopSong();
        this.isAlive = false;
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
        int startAni = state;

        if (moving) {
            state = RUNNING;
        } else {
            state = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0) {
                state = JUMP;
            } else {
                state = FALLING;
            }
        }

        if (startAni != state) {
            tickAnimation = 0;
            animationIndex = 0;
        }
    }

    private void updateAnimationTicks() {
        tickAnimation++;
        if (tickAnimation >= ANIMATION_SPEED) {
            tickAnimation = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(state)) {
                animationIndex = 0;
            }
        }
    }

    private void loadAnimations() {
        Image img = LoadSave.getSprite(LoadSave.PLAYER_ATLAS);
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
        right = false;
        jump = false;
    }

    public void setRunning(boolean value) {
        if (value) {
            playerSpeed *= 2;
        } else {
            playerSpeed /= 2;
        }
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public void setLeft(boolean left) {
        this.left = left;
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

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setAlive(boolean value) {
        this.isAlive = value;
    }

    public void dash(float value, double mouseX, double mouseY, int xLvlOffset) {
        this.dash = value;
        this.lastDashTime = System.currentTimeMillis();
        performDash(mouseX, mouseY, xLvlOffset);
    }

    public float getDashValue() {
        return this.dash;
    }

    public Point getSpawnPoint() {
        return this.spawPoint;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        facingRight = true;
        moving = false;
        state = IDLE;
        isAlive = true;
        jump = false;
        airSpeed = 0f;
        playerSpeed = 0.75f * Game.getScale();

        hitbox.setX(x);
        hitbox.setY(y);

        if (!isEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }
}
