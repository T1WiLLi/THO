package game.platformer.shader;

import java.awt.Point;
import java.util.Random;

import game.platformer.Game;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Particles {
    // Class for particles... Such as Rain, ash, dirt, dust. ect. We need an option
    // to disable it. Using JavaFX canvas.

    private Rain rain;

    public Particles() {
        this.rain = new Rain();
    }

    public void update(int xLvlOffset) {
        this.rain.update(xLvlOffset);
    }

    public void render(GraphicsContext gc, int xLvlOffset) {
        this.rain.render(gc, xLvlOffset);
    }

    public void setRainRendering() {
        this.rain.setDrawRainBoolean();
    }

    private final class Rain {
        private Point[] drops;
        private Random rand;
        private float rainSpeed = 1.25f;
        private Image rainParticle;
        private boolean renderRain = false;

        // Worth knowing, adding particles this way can cost a lot in computer power.
        // Disable it if the game lags.
        public Rain() {
            this.rand = new Random();
            this.drops = new Point[1000]; // We can also add less rain particles
            this.rainParticle = LoadSave.getSprite(LoadSave.RAIN_PARTICLE);
            initDrops();
            setDrawRainBoolean();
        }

        private void initDrops() {
            for (int i = 0; i < this.drops.length; i++) {
                this.drops[i] = getRndPos();
            }
        }

        public void update(int xLvlOffset) {
            for (Point point : this.drops) {
                point.y += this.rainSpeed;
                if (point.y >= Game.getScreenHeight()) {
                    point.y = -20;
                    point.x = (int) getNewX(xLvlOffset);
                }
            }
        }

        public void render(GraphicsContext gc, int xLvlOffset) {
            if (this.renderRain) {
                for (Point point : this.drops) {
                    gc.drawImage(this.rainParticle, (int) point.getX() - xLvlOffset, (int) point.getY(), 3, 12);
                }
            }
        }

        private Point getRndPos() {
            return new Point((int) getNewX(0), this.rand.nextInt(Game.getScreenHeight()));
        }

        private float getNewX(int xLvlOffset) {
            float value = (-Game.getScreenWidth()) + this.rand.nextInt((int) (Game.getScreenWidth() * 3f)) + xLvlOffset;
            return value;
        }

        public void setDrawRainBoolean() {
            if (rand.nextFloat() >= 0.8f) {
                renderRain = true;
            } else {
                renderRain = false;
            }
        }
    }
}
