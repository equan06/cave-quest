package byog.Core.World;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class BossRoom extends Room {
    public BossRoom(Position p, Random r) {
        super(p, 15, 15, Tileset.FLOOR, r);
    }

    /** Room should be circular with radius 10. */
    @Override
    public void draw(TETile[][] world) {
        for (int i = 0; i < base; i++) {
            for (int j = 0; j < height; j++) {

                int x = pos.getX() + i;
                int y = pos.getY() + j; // if position to be drawn is less than 10 units from center
                if (getCenterpos().diff(new Position(x, y)) <= 7) {
                    world[x][y] = getFloor();
                }
            }
        }
        this.assignID();
    }

}
