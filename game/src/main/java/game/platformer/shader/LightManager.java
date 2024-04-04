package game.platformer.shader;

import java.util.Random;

import game.platformer.Game;
import game.platformer.gamestate.Playing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class LightManager extends Canvas {

    private Playing playing;
    private GraphicsContext gc;
    private Lighting playerLight;

    private boolean isOn = false;

    public LightManager(Playing playing) {
        super(Game.getScreenWidth(), Game.getScreenHeight());
        this.gc = this.getGraphicsContext2D();
        this.playing = playing;
        this.playerLight = new Lighting(150.0f, 0.95f, new int[] { 0, 0, 0 });
        setLightBoolean();
    }

    public void update() {

    }

    public void render(int xLvlOffset) {
        if (isOn) {
            gc.clearRect(0, 0, getWidth(), getHeight());
            this.playerLight.render(this.gc, xLvlOffset, this.playing.getPlayer());
        }
    }

    public void setLightBoolean() {
        Random random = new Random();
        if (random.nextFloat() >= 0.9f) {
            isOn = true;
        } else {
            isOn = false;
        }
    }

    public void resetGraphicsContext() {
        gc.clearRect(0, 0, getWidth(), getHeight());
    }
}
