package game.platformer.objects;

import static game.platformer.utils.Constants.ObjectConstants.CHAIN_HEIGHT;
import static game.platformer.utils.Constants.ObjectConstants.CHAIN_WIDTH;
import static game.platformer.utils.Constants.ObjectConstants.SPIKE_BALL_HEIGHT;
import static game.platformer.utils.Constants.ObjectConstants.SPIKE_BALL_WIDTH;
import static game.platformer.utils.HelpMethods.canMoveHere;

import java.awt.Point;
import java.util.ArrayList;

import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpikeBall extends GameObject {

    private Image sprite;
    private Point closestAttachPoint;

    private double rotationAngle;
    private double rotationSpeed = 0.5;

    public SpikeBall(int x, int y, int objType) {
        super(x, y, objType);
        this.sprite = LoadSave.getSprite(LoadSave.SPIKE_BALL_OBJ);
        int adjustedX = x + (SPIKE_BALL_WIDTH / 2); // Adjusted X position for hitbox
        int adjustedY = y + (SPIKE_BALL_HEIGHT / 2); // Adjusted Y position for hitbox
        initHitbox((int) (SPIKE_BALL_WIDTH - (SPIKE_BALL_WIDTH * 0.4)),
                (int) (SPIKE_BALL_HEIGHT - (SPIKE_BALL_HEIGHT * 0.4)));
        hitbox.setX(adjustedX);
        hitbox.setY(adjustedY);
        this.rotationAngle = 0.0;
    }

    public Point findClosestAttachPoint(int x, int y, ArrayList<Point> attachPoints) {
        int closestDistance = Integer.MAX_VALUE;
        Point closestPoint = null;

        for (Point point : attachPoints) {
            int dx = point.x - x;
            int dy = point.y - y;
            int distanceSquared = dx * dx + dy * dy;
            if (distanceSquared < closestDistance) {
                closestDistance = distanceSquared;
                closestPoint = new Point(point.x + SPIKE_BALL_WIDTH / 2, point.y + SPIKE_BALL_HEIGHT / 2);
            }
        }
        // Generate the Chains itself.
        return closestPoint;
    }

    public void update(int[][] lvlData) {
        rotate(lvlData);
    }

    private void rotate(int[][] lvlData) {
        double attachX = closestAttachPoint.getX();
        double attachY = closestAttachPoint.getY();

        double nextAngle = rotationAngle + rotationSpeed;
        double radians = Math.toRadians(nextAngle);

        double newX = attachX + (x - attachX) * Math.cos(radians) - (y - attachY) * Math.sin(radians);
        double newY = attachY + (x - attachX) * Math.sin(radians) + (y - attachY) * Math.cos(radians);

        if (canMoveHere((float) newX, (float) newY, (float) hitbox.getWidth(), (float) hitbox.getHeight(), lvlData)) {
            hitbox.setX(newX);
            hitbox.setY(newY);
            rotationAngle = nextAngle;
        } else {
            rotationSpeed = -rotationSpeed;
        }
    }

    public void render(GraphicsContext gc, int xOffset) {
        double adjustedRotationAngle = rotationAngle * 2;
        gc.save();
        gc.translate(hitbox.getX() + SPIKE_BALL_WIDTH / 2 - xOffset - ((SPIKE_BALL_WIDTH / 2) / 2),
                hitbox.getY() + SPIKE_BALL_HEIGHT / 2 - ((SPIKE_BALL_HEIGHT / 2) / 2));
        gc.rotate(adjustedRotationAngle);
        gc.drawImage(sprite, -SPIKE_BALL_WIDTH / 2, -SPIKE_BALL_HEIGHT / 2, SPIKE_BALL_WIDTH, SPIKE_BALL_HEIGHT);
        gc.restore();

        renderChains(gc, xOffset);
    }

    private void renderChains(GraphicsContext gc, int xOffset) {
        if (closestAttachPoint != null) {
            double attachX = closestAttachPoint.getX();
            double attachY = closestAttachPoint.getY();

            double distance = Math.sqrt(Math.pow(x - attachX, 2) + Math.pow(y - attachY, 2));

            double directionX = (x - attachX) / distance;
            double directionY = (y - attachY) / distance;

            double rotatedDirectionX = directionX * Math.cos(Math.toRadians(rotationAngle))
                    - directionY * Math.sin(Math.toRadians(rotationAngle));
            double rotatedDirectionY = directionX * Math.sin(Math.toRadians(rotationAngle))
                    + directionY * Math.cos(Math.toRadians(rotationAngle));

            double chainStartX = attachX + ((SPIKE_BALL_WIDTH / 2 - CHAIN_WIDTH) / 2) / 2;
            double chainStartY = attachY + ((SPIKE_BALL_HEIGHT / 2 - CHAIN_HEIGHT) / 2) / 2;

            // Render the chains
            for (double i = 0; i < distance; i += CHAIN_WIDTH) {
                gc.drawImage(LoadSave.getSprite(LoadSave.CHAIN_OBJ), chainStartX - xOffset, chainStartY, CHAIN_WIDTH,
                        CHAIN_HEIGHT);

                chainStartX += rotatedDirectionX * CHAIN_WIDTH;
                chainStartY += rotatedDirectionY * CHAIN_HEIGHT;
            }
        }
    }

    public Point getClosestAttachPoint() {
        return closestAttachPoint;
    }

    public void setClosestAttachPoint(Point point) {
        this.closestAttachPoint = point;
    }
}
