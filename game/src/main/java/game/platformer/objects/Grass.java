package game.platformer.objects;

public class Grass extends GameObject {

    public Grass(int x, int y, int objType) {
        super(x, y, objType);
        initHitbox(0, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;

    }

    public int getType() {
        return this.objType;
    }
}
