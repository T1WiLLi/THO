package game.platformer.shader;

import game.platformer.Game;
import game.platformer.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class Lighting {
    private int[] rgbValue;
    private float lightRadius;
    private float filterAlpha;
    private boolean isOn;

    public Lighting(float lightRadius, float filterAlpha, int[] rgbValue) {
        this.lightRadius = Math.max(0, lightRadius);
        this.filterAlpha = Math.max(0, Math.min(1, filterAlpha));
        this.rgbValue = rgbValue;
        this.isOn = true;
    }

    public void update(float lightRadius, float filterAlpha, int[] rgbValue) {
        this.lightRadius = Math.max(0, lightRadius);
        this.filterAlpha = Math.max(0, Math.min(1, filterAlpha));
        this.rgbValue = rgbValue;
    }

    public <T extends Renderable> void render(GraphicsContext gc, int xLvlOffset, T source) {
        if (!isOn)
            return;

        double centerX = source.getHitbox().getX();
        double centerY = source.getHitbox().getY();

        RadialGradient radialGradient = new RadialGradient(
                0, 0,
                centerX - xLvlOffset + source.getHitbox().getWidth() / 2,
                centerY + source.getHitbox().getHeight() / 2,
                this.lightRadius,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(1, Color.rgb(rgbValue[0], rgbValue[1], rgbValue[2], this.filterAlpha)));

        gc.setFill(radialGradient);
        gc.setGlobalBlendMode(BlendMode.SOFT_LIGHT);
        gc.fillRect(0, 0, Game.getScreenWidth(), Game.getScreenHeight());

        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
    }

    public void setIsOn(boolean value) {
        this.isOn = value;
    }

    public boolean isOn() {
        return this.isOn;
    }
}
