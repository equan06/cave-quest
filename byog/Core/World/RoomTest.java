/*package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.TileEngine.TERenderer;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class RoomTest {

    public void roomTest() {
        Random random = new Random(1001020);
        Room room1 = new Room(new Position(), 4, 4, Tileset.WALL, Tileset.FLOOR, random);
        Room room2 = new Room(new Position(3, 3), 4, 4, Tileset.WALL, Tileset.FLOOR, random);
        Room room3 = new Room(new Position(5, 5), 4, 4, Tileset.WALL, Tileset.FLOOR, random);
        assertTrue(room1.overlaps(room2));
        assertFalse(room1.overlaps(room3));
    }

    public static void main(String[] args) {
        Random random = new Random(1001020);
        Room room1 = new Room(new Position(10, 20), 4, 4, Tileset.WALL, Tileset.FLOOR, random);
        Room room2 = new Room(new Position(20, 8), 6, 8, Tileset.WALL, Tileset.FLOOR, random);
        Room room3 = new Room(new Position(40, 40), 4, 4, Tileset.WALL, Tileset.FLOOR, random);
        Game game = new Game();
        game.createWorld();
        game.addRoom(room1);
        game.addRoom(room2);
   *//*     game.addRoom(room3);*//*
        game.createRoomMatrix();
        game.connectAll();
        game.renderWorld();
    }
}*/
