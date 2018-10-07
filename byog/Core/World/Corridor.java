package byog.Core.World;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Corridor extends Room {

    public Corridor(Position p, int b, int h, Random r) {
        super(p, b, h, r);
    }

    /**Draws the corridor on the world array.
     * Accounts for negative base and height, in which case
     * we draw the corridor in the opposite (-x or -y)
     * direction. The corridor must have one side that is
     * length 1. Note that after drawing, corridors are not
     * saved, so they are garbage collected.
     * @param world the world array */
    @Override
    public void draw(TETile[][] world) {
        if (!(this.height == 1 || base == 1)) {
            return;
        }

        if (base < 0) {
            for (int i = 0; i > base; i--) {
                world[pos.getX() + i][pos.getY()] = Tileset.FLOOR;
            }
        } else if (height < 0) {
            for (int j = 0; j > height; j--) {
                world[pos.getX()][pos.getY() + j] = Tileset.FLOOR;
            }
        } else {
            for (int i = 0; i < base; i++) {
                for (int j = 0; j < height; j++) {
                    world[pos.getX() + i][pos.getY() + j] = Tileset.FLOOR;
                }
            }
        }

    }
}
