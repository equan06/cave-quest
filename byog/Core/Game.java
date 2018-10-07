package byog.Core;

import byog.Core.Mobs.Player;
import byog.Core.World.World;
import byog.Core.World.Room;
import byog.TileEngine.Lighting;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;


import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

import java.io.*;
import java.util.Arrays;
import java.util.Random;


/**
 * Project was written during Sp2018 using Princeton StdDraw
 * libraries and an implementation of a 2d display (TETiles).
 *
 * @author Elliot Quan
 *
 * */
public class Game implements Serializable {

    private static Font defaultFont;

    private static final int SCALEFACTOR = 5;
    private static final int WIDTH = 16 * SCALEFACTOR; // 80
    private static final int HEIGHT = 9 * SCALEFACTOR - 6; // 39
    private static final int HUDWIDTH = WIDTH;
    private static final int HUDHEIGHT = 6;
    private static long SEED = 0;
    private static Random random = new Random(SEED);
    private static World world = null;
    private static Player player;
    private static HUD hud;
    private String saveState = "";
    private int floorCount;
    private int highScore = 1;
    private int currScore = 0;
    private boolean saveFound = false;
    private boolean render = true;
    private boolean quit = false;
    private boolean devMode = false;





    /**
     *  - add mob pathfinding (if close to player, attempt to move towards)
     *  - bugfix
     *
     *  - organize variables
     *  - organize methods/javadocs
     *  - bells and whistles
     *  - more cohesive bugtests?
     * */

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        resetGameSettings();
        loadHigh();
        render = true;
        defaultFont = new Font("Monaco", Font.BOLD, 14);
        TERenderer ter = showMenu();
        String menuResult = menuOptions("");
        if (menuResult.equals("nosave")) {
            System.exit(0);
        } else {
            if (!saveFound) {
                createWorld(null);
            }
            createHUD(ter);
            renderWorld(ter);
            renderHUD();
            while (!quit) {
                gameTurn("");
                hud.updateWorld(world);
                renderWorld(ter);
                renderHUD();
            }
            boolean newHigh = calculateScore();
            if (player.isDead()) {
                eraseSave();
                showDeathScreen(newHigh);
            } else {
                showVictoryScreen(newHigh);
            }
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For exampqqle "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        resetGameSettings();
        saveState = input;
        input = menuOptions(input);
        if (saveFound) {
            saveState += input;
        } else {
            random = new Random(SEED);
            createWorld(null);
        }
        while (input.length() > 0) {
            input = gameTurn(input);
        }
        return TETile.copyOf(world.getWorld());
    }


    /** Reset all nonfinal values to default values.*/
    public void resetGameSettings() {
        SEED = 0;
        random = null;
        world = null;
        player = null;
        hud = null;
        saveState = "";
        floorCount = 1;
        highScore = 0;
        currScore = 0;
        saveFound = false;
        render = false;
        quit = false;
        devMode = false;
        Room.resetIDCount();
    }


    /** Draws all menu options using StdDraw methods. */
    public TERenderer showMenu() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + HUDHEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.picture(WIDTH / 2, HEIGHT * .5, "/byog/img/title.png");
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 60));
        StdDraw.text(WIDTH * .5, HEIGHT * .75, "Cave Quest");
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 40));
        centerText(.45, "New Game (N)");
        centerText(.35, "Load Game (L)");
        centerText(.25, "Quit (Q)");
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        centerText(.1, "By E. Q. @2018");
        StdDraw.show();
        StdDraw.clear(Color.BLACK);
        return ter;
    }


    /** Displays after death. Can return to Main Menu or exit game. */
    public void showDeathScreen(boolean newHigh) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH / 2, HEIGHT * .55, 10, 10);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.rectangle(WIDTH / 2, HEIGHT * .55, 10, 10);
        centerText(.75, "You died on floor " + floorCount + ".");
        if (newHigh) {
            centerText(.7, "Your score: " + currScore + ". New High Score!");
        } else {
            centerText(.7, "Your score: " + currScore);
            centerText(.65, "Highest score: " + highScore);
        }
        centerText(.5, "Mobs killed: " + player.getKillcount());
        centerText(.45, "Main Menu (M)");
        centerText(.4, "Quit (Q)");
        StdDraw.show();
        char c = waitForInput("MmQq");
        if (c == 'Q' || c == 'q') {
            System.exit(0);
        } else {
            playWithKeyboard();
        }
    }

    /** Displays after defeating final boss. Can return to Main Menu or exit game. */
    public void showVictoryScreen(boolean newHigh) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH / 2, HEIGHT * .55, 15, 15);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.rectangle(WIDTH / 2, HEIGHT *.55, 15, 15);
        centerText(.75, "You stagger through the portal.");
        StdDraw.show();
        StdDraw.pause(1000);
        centerText(.7, "You open your eyes. You can't see anything.");
        StdDraw.show();
        StdDraw.pause(1000);
        centerText(.65, "Something knocks you out cold.");
        StdDraw.show();
        StdDraw.pause(1000);
        centerText(.6, "...");
        StdDraw.show();
        StdDraw.pause(2000);
        centerText(.55, "To be continued...");
        StdDraw.show();
        StdDraw.pause(2000);
        if (newHigh) {
            centerText(.5, "Your score: " + currScore + ". New High Score!" );
        } else {
            centerText(.5, "Your score: " + currScore);
            centerText(.45, "Highest score: " + highScore);
        }
        centerText(.4, "Play Again (M) ");
        centerText(.35, "Quit (Q)");
        StdDraw.show();
        char c = waitForInput("MmQq");
        if (c == 'Q' || c == 'q') {
            System.exit(0);
        } else {
            playWithKeyboard();
        }
    }
    
    public void centerText(double factor, String text) {
        StdDraw.text(WIDTH / 2, HEIGHT * factor, text);
    }

    /** Player's current score calculated after death/victory. If higher than previous
     * score, save current score as new high score. */
    public boolean calculateScore() {
        currScore = floorCount * 100 + player.getKillcount() * 10;
        if (currScore > highScore) {
            highScore = currScore;
            saveHigh();
            return true;
        } else {
            return false;
        }
    }



    /**
     * Used for soliciting keyboard input.
     * Waits until a char contained in String "chars" is entered.
     * Returns the first valid char entered, and adds it to the saveState
     * (String of all entered commands).
     *
     * @param chars the String of valid char inputs
     * @return the entered char
     * */
    public char waitForInput(String chars) {
        char c = '•';
        while (chars.indexOf(c) == -1) {
            if (world != null) {
                int xMouse = (int) StdDraw.mouseX();
                int yMouse = (int) StdDraw.mouseY();
                if (xMouse < WIDTH && yMouse < HEIGHT) {
                    String tile = world.getTileDescription(xMouse, yMouse);
                    if (tile.equals("")) {
                        hud.drawTileDescription("          ");
                    } else {
                        hud.drawTileDescription(tile);
                    }
                    renderHUD();
                }
            }
            if (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
                if (chars.indexOf(c) >= 0) {
                    saveState += Character.toString(c);
                }
            }
        }
        return c;
    }


    /**
     * Manages the MENU module.
     *
     * If an inputString is provided, the first character must be a menu command,
     * which it then executes and passes the rest of the inputString.
     *
     * Otherwise, it solicits keyboard input.
     *
     * "N" - New Game
     * "L" - Load Game
     * "Q" - Quit Game
     *
     * If Load is selected and no save file (save.txt) is found, the game will exit.
     *
     * @param inputString string to be processed if using playWithInputString()
     * @return the processed inputString if New Game is selected, or an empty string.
     *         if Load is selected, then it passes control to the Load module, which
     *         handles how the game is loaded.
     */
    public String menuOptions(String inputString) {
        char input;
        if (inputString.length() > 0) {
            input = inputString.charAt(0);
            inputString = inputString.substring(1);
        } else {
            input = waitForInput("NnLlQq");
        }

        if (input == 'N' || input == 'n') {
            inputString = seedOptions(inputString, "");
        } else if (input == 'L' || input == 'l') {
            load();
            if (!saveFound) {
                return "nosave";
            }
        } else if (input == 'Q' || input == 'q') {
            System.exit(0);
        }
        return inputString;
    }

    /**
     * Manages the SEED module.
     *
     * If playWithInputString() is used, this method receives an inputString and processes it.
     * It sets the seed to be the string of numbers up to the first appearance of escape key 'S'.
     *
     * If playWithKeyboard() is used, the empty string will be passed. Then, it will
     * solicit number inputs (0-9) until the 'S' key is pressed. When 'S' is
     * pressed, the SEED is stored as a long and the module terminates. If the SEED is too
     * large, it will restart the process. If no numbers are inputted and 'S' is given,
     * 0 will be the default SEED.
     *
     * @param inputString string of commands if running game via playWithInputString() .
     * @param seed string containing already entered digits of the seed.
     * @return processed inputString (all chars up to S deleted). empty if no inputString
     */
    public String seedOptions(String inputString, String seed) {
        if (render) {
            drawSeedText(WIDTH / 2, HEIGHT / 2, seed);
        }

        char input;
        if (inputString.length() > 0) {
            input = inputString.charAt(0);
            inputString = inputString.substring(1);
        } else {
            input = waitForInput("Ss0123456789");
        }

        if (input == 'S' || input == 's') {
            if (seed.length() == 0) {
                SEED = 0;
            } else {
                try {
                    SEED = Long.parseLong(seed);
                } catch (NumberFormatException n) {
                    seed = "";
                    if (render) {
                        drawSeedText(WIDTH / 2, HEIGHT / 2, "Seed too long!");
                        StdDraw.pause(500);
                    }
                    return seedOptions(inputString, seed);
                }
            }
            random = new Random(SEED);
            return inputString;
        } else {
            seed += Character.toString(input);
            if (render) {
                drawSeedText(WIDTH / 2, HEIGHT / 2, seed);
            }
            return seedOptions(inputString, seed);
        }
    }

    /**
     * Draws text for the SEED module. Used to display currently entered characters
     * and error messages.
     * @param x text centered at x
     * @param y text centered at y
     * @param s String to be displayed
     */
    public void drawSeedText(double x, double y, String s) {
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 50));
        centerText(.75, "Enter a seed. ");
        centerText(.65, "Press S when done. ");
        StdDraw.text(x, y, s);
        StdDraw.show();
        StdDraw.clear(Color.BLACK);
    }

    /**
     * Creates a new random World instance. Used to create the first world,
     * or create new world levels. If no player object is passed to it, a new
     * player will be created. Otherwise, it keeps a copy of the current player.
     *
     * @param p the player object to be preserved
     * */
    public void createWorld(Player p) {
        world = new World(WIDTH, HEIGHT, floorCount, random);
        world.generateWorld();
        player = world.spawnPlayerAndMobs(p, true);
        if (render && !devMode) {
            Lighting.darkenWorld(world.getWorld());
        }
    }

    /** Generate a normal level. */
    public void createNewLevel() {
        floorCount++;
        createWorld(player);
    }

    /** Generate a special boss level. */
    public void createBossLevel() {
        floorCount++;
        world = new World(WIDTH, HEIGHT, floorCount, random);
        world.generateBossWorld();
        player = world.spawnPlayerAndMobs(player, false);
        world.spawnBossMobs();
        if (render && !devMode) {
            Lighting.darkenWorld(world.getWorld());
        }
    }



    /**
     * Uses TERenderer's renderFrame method to render the world.
     * Should be called only after every player input. Updates
     * the HUD as well. The HUD must be created before this method runs.
     * Show() is only called by renderFrame. */
    public void renderWorld(TERenderer t) {
        StdDraw.setFont(defaultFont);
        TETile[][] w = world.getWorld();
        if (!devMode) {
            Lighting.darkenPos(w, player.getPos());
            Lighting.brightenPos(w, player.getPos());
        }
        t.renderFrame(w);
    }

    public void renderHUD() {
        StdDraw.setFont(defaultFont);
        hud.updateHud();
        hud.getTer().renderHUD(hud.getHud());
    }

    /**
     * Instantiates the HUD. Must be run after the player is created,
     * and before the HUD is rendered.*/
    public void createHUD(TERenderer t) {
        hud = new HUD(HUDWIDTH, HUDHEIGHT, world, t);
    }

    /**
     * Manages the GAME TURN module.
     *
     *
     * If playWithInputString() is used, a valid inputString is provided with player commands.
     * The inputString will be processed until it is empty. It then sets boolean end to true,
     * signalling that the inputString has been processed.
     *
     * If playWithKeyboard() is used, an empty inputString will be provided. It will wait for
     * and process input until ":Q" is encountered. Then, it will give control to the SAVE module.
     *
     * Valid player commands are:
     * "WASD" - movement
     * "~" - toggle player position on the HUD
     * ":Q" - save and quit game
     *
     * If render is true, the updated world state will be rendered.
     *
     * FUTURE: if I ever return to this project, break up the module into smaller
     * parts.
     *
     * @param inputString Str containing player cmds if using playWithInputString()
     * */
    public String gameTurn(String inputString) {

        char input;
        if (inputString.length() > 0) {
            input = inputString.charAt(0);
            inputString = inputString.substring(1);
            if (inputString.length() == 0 && !render) {
                quit = true;
            }
        } else if (!render) {
            return inputString;
        } else {
            input = waitForInput("wWaAsSdD~:QqpP");
        }
        if ((input == 'Q' || input == 'q') && saveState.charAt(saveState.length() - 2) == ':') {
            if (devMode) {
                toggleDevMode();
            }
            saveHigh();
            saveAndQuit();
        } else if (input == 'Q' || input == 'q' || input == ':') {
            return inputString;
        } else if (render && input == '~') {
            toggleDevMode();
        } else {
            // Player turn
            String playerMove = player.move(input, world);

            String objectFound = player.closeToWorldObjects(world);
            if (render && objectFound.equals("portal")) {
                hud.addNewMsg("You find a glowing portal. You wonder where it leads.");
            } else if (render && objectFound.equals("campfire")) {
                hud.addNewMsg("You find a burning campfire. You wonder who built it.");
            } else if (render && objectFound.equals("freeze")) {
                hud.addNewMsg("You find a magic stone. Should you touch it?");
            } else if (render && playerMove.equals("create")) {
                if (floorCount == 12) {
                    quit = true;
                    return "";
                } else if (floorCount == 4 || floorCount == 7 || floorCount == 10) {
                    createBossLevel();
                    hud.addNewMsg("You descend.");
                    hud.addNewMsg("You have a bad feeling about this.");
                    if (devMode) {
                        devMode = false;
                        Lighting.darkenWorld(world.getWorld());
                    }
                } else {
                    createNewLevel(); // create new lvl, pass curr player object to new level
                    hud.addNewMsg("You descend.");
                    randomLoadMsg();
                    if (devMode) {
                        devMode = false;
                        Lighting.darkenWorld(world.getWorld());
                    }
                }
            } else if (render && playerMove.equals("rest")) {
                world.destroyCampfire();
                hud.addNewMsg("You rest for a while. You feel invigorated.");
            } else if (render && playerMove.equals("freeze")) {
                if (player.checkHasFreeze()) {
                    hud.addNewMsg("You touch the magic rock. Your hands become very cold.");
                } else {
                    hud.addNewMsg("You touch the magic rock. Your hands become warm again.");
                }
            } else if (render && playerMove.length() > 0) {
                hud.addNewMsg(playerMove);
            } else if (playerMove.equals("create")) { // note - bug if playwithinputstring encounters rest, freeze, or bossLevel. will fix later
                if (floorCount == 10) {
                    return inputString;
                } else {
                    createNewLevel();
                }

            }
            // Mob turns
            if (render) {
                world.mobTurns(hud);
            } else {
                world.mobTurns(null);
            }

            // possibly bug if random inputString generated and
            // player dies before inputString finished.
            if (player.isDead()) {
                quit = true;
            }

            if (render) {
                if (playerMove.length() > 0) {
                    return playerMove;
                }
            }
        }
        return inputString;
    }

    /** Toggles Dev Mode, making entire floor visible. */
    public void toggleDevMode() {
        devMode = !devMode;

        world.toggleWorldObjectsVisible();
        world.toggleMobsVisible();
        if (devMode) {
            Lighting.brightenWorld(world.getWorld());
            hud.addNewMsg("##Dev mode activated##");
        } else {
            Lighting.darkenWorld(world.getWorld());
            hud.addNewMsg("##Dev mode deactivated##");
        }
    }

    /** Load messages displayed in HUD that occur upon entering a new floor. */
    public void randomLoadMsg() {
        int num = RandomUtils.uniform(random, 0, 6);
        String msg;
        switch (num) {
            case 1:
                msg = "You feel cold.";
                break;
            case 2:
                msg = "You feel a shiver down your spine.";
                break;
            case 3:
                msg = "You hear faint shrieks in the distance.";
                break;
            case 4:
                msg = "Your head aches.";
                break;
            case 5:
                msg = "You stand determined.";
                break;
            default:
                msg = "You hear the steady beat of your heart.";
        }
        hud.addNewMsg(msg);
    }



    /**
     * Manages the SAVE module.
     *
     * Saves and quits the game. Uses Serializable to store the World object and floorCount.
     * Everything inside World is also Serializable, so those are stored as well.
     *
     */
    public void saveAndQuit() {
        try {
            FileOutputStream file = new FileOutputStream("./save.txt");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(world);
            out.writeObject(floorCount);
            file.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Something went wrong. ");
        }
        if (render) {
            System.exit(0);
        }
    }


    /**
     * Manages the LOAD module.
     *
     * Loads the game from "save.txt". Retrieves player and random
     * variables from the serialized World object.
     */
    public void load() {
        String filename = "./save.txt";
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            world = (World) in.readObject();
            player = world.getPlayer();
            random = world.getRandom();
            floorCount = (Integer) in.readObject();
            file.close();
            in.close();
            saveFound = true;
        } catch (IOException e) {
            System.out.println("No save found.");
        } catch (ClassNotFoundException c) {
            System.out.println("No class found.");
        }
    }


    /**
     * Deletes "save.txt". Only occurs if the player dies,
     * preventing future loading.
     */
    public void eraseSave() {
        File file = new File("./save.txt");
        if (file.delete()) {
            System.out.println("File deleted.");
        } else {
            System.out.println("No deletion.");
        }
    }

    public void loadHigh() {
        String filename = "./score.txt";
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            highScore = (Integer) in.readObject();
            file.close();
            in.close();
        } catch (IOException e) {
            highScore = 0;
        } catch (ClassNotFoundException c) {
            System.out.println("No class found.");
        }
    }

    public void saveHigh() {
        try {
            FileOutputStream file = new FileOutputStream("./score.txt");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(highScore);
            file.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Something went wrong. ");
        }
    }








    /** TEST METHODS */
    public boolean testEquals() {
        TETile[][] w1 = playWithInputString("N234S");
        TETile[][] w2 = playWithInputString("N234S");
        return Arrays.deepEquals(w1, w2);
    }

    public boolean playTwice(String inputString) {
        TETile[][] w1 = playWithInputString(inputString);
        TETile[][] w2 = playWithInputString(inputString);
        return Arrays.deepEquals(w1, w2);
    }

    /** Renders a specific world instance. For testing. */
    public void renderWorldInstance(TETile[][] instance) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH + 1, HEIGHT + 6, 1, 5);
        defaultFont = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(defaultFont);
        ter.renderFrame(instance);
    }
}
