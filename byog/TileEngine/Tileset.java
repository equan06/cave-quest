package byog.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {

    public static final Color backgroundColor = new Color(0, 0, 25);




    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "");


    public static final TETile PLAYER = new TETile('@', Color.pink, backgroundColor, "player");
    public static final TETile GOBLIN = new TETile('G', Color.green, backgroundColor, "goblin");
    public static final TETile SKELETON = new TETile('S', Color.white,
            backgroundColor, "skeleton");
    public static final TETile ROCKGOLEM = new TETile('R', new Color(204, 153, 0),
            backgroundColor, "rock golem");
    public static final TETile MAGE = new TETile('M', Color.RED,
            backgroundColor, "archmage");
    public static final TETile DEMON = new TETile('D', Color.RED,
            backgroundColor, "demon");
    public static final TETile ARCHDEMON = new TETile('A', Color.RED,
                                                        backgroundColor, "demon");



    public static final TETile GRASS = new TETile('"', new Color(102, 153, 255), backgroundColor, "grass");
    public static final TETile FLOWER = new TETile('❀', new Color(0, 85, 255), backgroundColor, "flower");


    public static final TETile WALL = new TETile('▓',
            Color.black, new Color(179, 179, 204), "wall");
    public static final TETile WALL1 = new TETile('▓', new Color(82, 82, 122), Color.black,
             "wall");
    public static final TETile WALL2 = new TETile('▓', new Color(92, 92, 138), Color.black,
             "wall");
    public static final TETile WALL3 = new TETile('▓', new Color(102, 102, 153), Color.black,
             "wall");
    public static final TETile WALL4 = new TETile('▓', new Color(117, 117, 163), Color.black,
             "wall");
    public static final TETile FLOOR = new TETile('░', new Color(0, 43, 128),
            backgroundColor, "floor");
    public static final TETile PORTAL = new TETile('⋂', Color.white,
            backgroundColor, "portal");
    public static final TETile CAMPFIRE = new TETile('♥', Color.pink,
            backgroundColor, "campfire");

    public static final TETile FREEZE = new TETile('◆', Color.white,
            backgroundColor, "magic rock");
    public static final TETile HUDBORDER = new TETile('"',
            new Color(148, 77, 255), Color.black, "hudborder");


    Color floorColor = new Color(128, 128, 255);







    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");

    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
}


