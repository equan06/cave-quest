/*
package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Sword implements Item {

    public Position pos;
    TETile sprite = Tileset.MOUNTAIN; // temporary
    TETile temp = Tileset.FLOOR;
    public final String description = "A mighty blade.";

    public Sword(Position p) {
        pos = p;
    }

    @Override
    public void spawn(TETile[][] world, Position p) {
        if (world[p.x][p.y] != Tileset.FLOOR) {
            return;
        } else {
            temp = world[p.x][p.y];
            world[p.x][p.y] = sprite;
        }
    }

    @Override
    public void action() {
        return;
    }

    @Override
    public String examine() {
        return description;
    }
}
*/
