package game.platformer.objects;

import game.platformer.Game;

public class Spike extends GameObject {
    public Spike(int x, int y, int objType) {
        super(x, y, objType);
        initHitbox(32, 16);
        xDrawOffset = 0;
        yDrawOffset = (int) (Game.getScale() * 16);
        hitbox.setY(hitbox.getY() + yDrawOffset);
    }
}
