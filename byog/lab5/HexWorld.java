package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld extends RandomWorldDemo {

    public static final int HEIGHT = 30;
    public static final int WIDTH = 60;
    /** Add a hexagon of side length s starting at the bottom left corner at xpos/ypos */
    public static void addHexagon(int s, int x, int y, TETile[][] world) {

    }

    /** Draw a left equilateral triangle of size s starting from x, y */
    public static void drawTopLeftTriangle(int s, int x, int y, TETile[][] world, TETile tile) {
        for (int i = 0; i < s; i++) {
            for (int j = 0; j <= i; j++) {
                world[x + i][y + j] = tile;
            }
        }
    }
    public static void drawTopRightTriangle(int s, int x, int y, TETile[][] world, TETile tile) {
        for (int i = 0; i < s; i++) {
            for (int j = s - 1 - i ; j >= 0; j--) {
                world[x + i][y + j] = tile;
            }
        }
    }
    public static void drawBotLeftTriangle(int s, int x, int y, TETile[][] world, TETile tile) {
        for (int i = 0; i < s; i++) {
            for (int j = 0 ; j <= i; j++) {
                world[x + i][y + s - j - 1] = tile;
            }
        }
    }

    public static void drawBotRightTriangle(int s, int x, int y, TETile[][] world, TETile tile) {
        for (int i = 0; i < s; i++) {
            for (int j = i ; j < s; j++) {
                world[x + i][y + j] = tile;
            }
        }
    }
    public static void drawRectangle(int b, int h, int x, int y, TETile[][] world, TETile tile) {
        for (int i = 0; i < b; i++) {
            for (int j = 0; j < h; j++) {
                world[x + i][y + j] = tile;
            }
        }
    }

    public static void drawHexagon(int s, int x, int y, TETile[][] world, TETile tile) {
        drawBotLeftTriangle(s, x, y, world, tile);
        drawRectangle(s - 2, 2 * s, x + s, y, world, tile);
        drawBotRightTriangle(s, x + 2 * s - 2, y, world, tile);

        drawTopLeftTriangle(s, x, y + s, world, tile);
        drawTopRightTriangle(s, x + 2 * s - 2, y + s, world, tile);

    }
    public static void fillNothing(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }
    public static void main(String[] args) {
        TERenderer render = new TERenderer();
        render.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillNothing(world);
        TETile wall = Tileset.WALL;
        drawHexagon(8, 10,5, world, wall);
        render.renderFrame(world);
    }

}
