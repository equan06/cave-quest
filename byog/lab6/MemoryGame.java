package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        String res = "";
        for (int i = 0; i < n; i++) {
            res += CHARACTERS[(int)(Math.random() * n)];
        }
        return res;
    }

    public void drawFrame(String s) {
        StdDraw.clear();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.setPenColor();
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
        StdDraw.pause(2000);
    }

    public void flashSequence(String letters) {
        char[] chars = letters.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            drawFrame(Character.toString(chars[i]));
            StdDraw.clear();
            StdDraw.show();
            System.out.println("hello?");
            StdDraw.pause(2000);
        }
    }

    public String solicitNCharsInput(int n) {
        int i = 0;
        char c = 0;
        String s = "";
        while (i < n) {
            if (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
                System.out.println("smoething happnd");
                s += Character.toString(c);
                drawFrame(s);
                i++;
            }
        }
        return s;
    }

    public void startGame() {
        int round = 1;
        while (round < 10) {
            drawFrame("Round: " + round);
            String randstring = generateRandomString(round);
            flashSequence(randstring);
            drawFrame("Type your input now.");
            String playerinput = solicitNCharsInput(round);
            if (playerinput.equals(randstring)) {
                drawFrame("Good job!");
                round += 1;
            } else {
                drawFrame("Game over! You made it to round: " + round);
                round = 11;
            }
        }

    }

}
