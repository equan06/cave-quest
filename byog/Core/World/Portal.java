package byog.Core.World;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Portal extends WorldObject {

    private static final long serialVersionUID = 1111111111112L;
    private static String description = "You wonder where it goes.";

    public Portal(Position p) {
        super(p);
        sprite = Tileset.PORTAL;
        hiddenSprite = TETile.darkenTile(Tileset.FLOOR);
    }
}
