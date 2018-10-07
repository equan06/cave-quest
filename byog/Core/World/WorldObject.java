package byog.Core.World;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public abstract class WorldObject implements Serializable {

    protected Position pos;
    protected TETile sprite;
    protected TETile hiddenSprite = TETile.darkenTile(Tileset.FLOOR);
    protected String description;
    private boolean visible;
    private boolean found; // if found, then toggleVisible should not hide object.

    public WorldObject(Position p) {
        pos = p;
        visible = false;
        found = false;
    }

    public Position getPos() {
        return pos;
    }

    public boolean isVisible() {
        return visible;
    }

    public void toggleVisible() {
        visible = !visible;
    }

    public void setFound() {
        found = true;
    }

    public void show(TETile[][] world) {
        visible = true;
        spawn(world);
    }

    public void spawn(TETile[][] world) {
        if (visible || found) {
            world[pos.getX()][pos.getY()] = sprite;
        } else {
            world[pos.getX()][pos.getY()] = hiddenSprite;
        }
    }

}
