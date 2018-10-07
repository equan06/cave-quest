package byog.Core.World;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class FreezeStone extends WorldObject {
    private static final long serialVersionUID = 1111111111212L;
    private static String description = "A magical stone.";

    public FreezeStone(Position p) {
        super(p);
        sprite = Tileset.FREEZE;
        hiddenSprite = TETile.darkenTile(Tileset.FLOOR);
    }
}
