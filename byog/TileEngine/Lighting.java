package byog.TileEngine;

import byog.Core.World.Position;

public class Lighting {

    /**
     * Apply darkness to the world.
     * @param world TETile[][] world array.
     */
    public static void darkenWorld(TETile[][] world) {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if (!world[i][j].equals(Tileset.NOTHING)) {
                    world[i][j] = TETile.darkenTile(world[i][j]);
                }
            }
        }
    }

    /**
     * Apply light to the world.
     * @param world TETile[][] world array.
     */
    public static void brightenWorld(TETile[][] world) {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if (!world[i][j].equals(Tileset.NOTHING)) {
                    world[i][j] = TETile.brightenTile(world[i][j]);
                }
            }
        }
    }




    /**
     * Apply lighting to a circle of radius 5 around Position p.
     * @param world TET[][] world array
     * @param p center of position
     */
    public static void brightenPos(TETile[][] world, Position p) {
        int centerX = p.getX();
        int centerY = p.getY();
        int x = p.getX() - 5;
        int y = p.getY() - 5;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (x + i >= 0 && y + j >= 0 && (x + i) < world.length
                        && (y + j) < world[0].length
                        && !world[x + i][y + j].equals(Tileset.NOTHING)) {
                    int dx = centerX - (x + i); // basic circle algorithm
                    int dy = centerY - (y + j);
                    if (dx * dx + dy * dy <= 25) {
                        world[x + i][y + j] = TETile.brightenTile(world[x + i][y + j]);
                    }
                }
            }
        }
    }

    public static void darkenPos(TETile[][] world, Position p) {
        int x = p.getX() - 6;
        int y = p.getY() - 6;
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                if ((x + i >= 0) && (y + j >= 0) && (x + i) < world.length
                        && (y + j) < world[0].length
                        && !world[x + i][y + j].equals(Tileset.NOTHING)) {
                    world[x + i][y + j] = TETile.darkenTile(world[x + i][y + j]);
                }
            }
        }
    }
}
