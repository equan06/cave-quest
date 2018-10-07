package byog.Core.World;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Campfire extends WorldObject {

    private static final long serialVersionUID = 1111111111113L;
    private static String description = "You can rest here.";

    public Campfire(Position p) {
        super(p);
        sprite = Tileset.CAMPFIRE;
        hiddenSprite = TETile.darkenTile(Tileset.FLOOR);
    }


    public void burnOut() {
        sprite = hiddenSprite;
    }

}
