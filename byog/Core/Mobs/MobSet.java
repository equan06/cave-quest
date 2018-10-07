package byog.Core.Mobs;


import java.io.Serializable;

public class MobSet implements Serializable {
    public static final String[] MOBS = {"Goblin", "Skeleton", "Rock Golem", "Demon"};
    private static final long serialVersionUID = 1111111111123L;
    /**
     * Takes a floor number and returns a random mob according to the floor number.
     *
     *
     * @param floor
     * @return
     */
    public static String[] randomMobArray(int floor) {
        String[] validMobs;
        if (floor >= 8) {
            validMobs = new String[4];
            System.arraycopy(MOBS, 0, validMobs, 0, 4);
        }
        else if (floor >= 6) {
            validMobs = new String[3];
            System.arraycopy(MOBS, 0, validMobs, 0, 3);
        } else if (floor >= 3) {
            validMobs = new String[2];
            System.arraycopy(MOBS, 0, validMobs, 0, 2);
        } else {
            validMobs = new String[1];
            System.arraycopy(MOBS, 0, validMobs, 0, 1);
        }
        return validMobs;
    }
}
