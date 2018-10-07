package byog.Core;

import byog.Core.Mobs.Player;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.Core.World.World;

import java.awt.Color;

public class HUD {
    private TETile[][] hud;
    private final int HEIGHT;
    private final int WIDTH;
    private World world;
    private TERenderer ter;
    private StringArrayDeque messages = new StringArrayDeque();

    public HUD(int width, int height, World w, TERenderer t) {
        WIDTH = width;
        HEIGHT = height;
        hud = new TETile[WIDTH][HEIGHT];
        fillBorder();
        fillHUD();
        world = w;
        ter = t;
        drawPlayerInfo();
    }

    public TETile[][] getHud() {
        return hud;
    }

    public TERenderer getTer() {
        return ter;
    }

    public void updateWorld(World w) {
        world = w;
    }
    /**
     * Updates the HUD. Should update:
     * - player info (hp)
     * - mouse pointer description
     * - enemies in room, and current HP
     * */
    public void updateHud() {
        drawPlayerInfo();
        drawMessages();
        drawFloor();
    }

    public void addNewMsg(String msg) {
        messages.enqueue(msg);
    }


    public void fillBorder() {
        for (int i = 0; i < WIDTH; i++) {
            hud[i][0] = Tileset.HUDBORDER;
            hud[i][HEIGHT - 1] = Tileset.HUDBORDER;
        }
        for (int j = 0; j < HEIGHT; j++) {
            hud[0][j] = Tileset.HUDBORDER;
            hud[WIDTH - 1][j] = Tileset.HUDBORDER;
        }
    }

    public void fillHUD() {
        for (int i = 1; i < WIDTH - 1; i++) {
            for (int j = 1; j < HEIGHT - 1; j++) {
                hud[i][j] = Tileset.NOTHING;
            }
        }
    }

    public TETile charToTile(char c) {
        return new TETile(c, Color.WHITE, Color.BLACK, "");
    }

    public TETile charToTile(char c, Color color) {
        return new TETile(c, color, Color.BLACK, "");
    }

    /**
     * Take a string and insert it into the hud at pos x, y of the HUD.
     * Since the hud has a border of 1 tile, x and y must be at least 1.
     * @param input
     */
    public void drawStringToHud(String input, int x, int y) {
        for (int i = 0; i < input.length(); i++) {
            hud[x + i][y] = charToTile(input.charAt(i));
        }
    }

    public void drawStringToHud(String input, int x, int y, int intensity) {
        Color color;
        switch (intensity) {
            case 3: color = new Color(102, 204, 255);
                break;
            case 2: color = new Color(51, 187, 255);
                break;
            case 1: color = new Color(0, 136, 204);
                break;
            default: color = new Color(0, 102, 153);
                break;
        }
        for (int i = 0; i < input.length(); i++) {
            hud[x + i][y] = charToTile(input.charAt(i), color);
        }
    }


    /**
     * Clear HUD at pos x, y by setting all tiles from
     * [x,y] to [x+length-1][y] to NOTHING.
     * */
    public void clearHudArea(int length, int x, int y) {
        for (int i = 0; i < length; i++) {
            hud[x + i][y] = Tileset.NOTHING;
        }
    }


    /**
     * Health should be drawn at 1, HEIGHT - 2 (top left corner).
     */
    public void drawPlayerInfo() {
        Player p = world.getPlayer();
        TETile sprite = p.getSprite();

        clearHudArea(10, 1, HEIGHT - 2);
        drawStringToHud("HP:" + p.getHealthString(), 1, HEIGHT - 2);
        clearHudArea(10, 1, HEIGHT - 3);
        drawStringToHud("Atk:" + p.getAtk(), 1, HEIGHT - 3);
        clearHudArea(10, 1, HEIGHT - 4);
        drawStringToHud("Lv:" + p.getLevel(), 1, HEIGHT - 4);
        clearHudArea(10, 1, HEIGHT - 5);
        drawStringToHud("Xp:" + p.getXpString(), 1, HEIGHT - 5);
        clearHudArea(10, 12, HEIGHT - 3);
        drawStringToHud("Kc:" + p.getKillcount(), 12, HEIGHT - 3);
    }

    /**
     * Description should be drawn at WIDTH / 2 - 10, HEIGHT - 2
     */
    public void drawTileDescription(String d) {
        clearHudArea(10, 12, HEIGHT - 2);
        drawStringToHud(d, 12, HEIGHT - 2);
    }

    /**
     * Level should be drawn at 1, 1
     */
    public void drawFloor() {
        clearHudArea(10, 12, HEIGHT - 5);
        drawStringToHud("Flr:" + world.getFloor(), 12, HEIGHT - 5);
    }

    public void drawMessages() {
        for (int i = 0; i < 4; i++) {
            clearHudArea(56, 22, HEIGHT - 2 - i);
        }
        int i = 0;
        for (String msg : messages) {
            drawStringToHud(msg, 22, HEIGHT - 2 - i, i);
            i++;
        }
    }

/*    public void drawObjective() {
        for (int i = 0; i < 4; i++) {
            clearHudArea(22, 55, HEIGHT - 2 - i);
        }
        drawStringToHud("Objective: ", 55, HEIGHT - 2, 3);
        drawStringToHud("Find the room portal.", 55, HEIGHT - 3, 3);
        drawStringToHud("Kill all enemies.", 55, HEIGHT - 4, 3);
    }*/



}
