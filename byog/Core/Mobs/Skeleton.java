package byog.Core.Mobs;

import byog.TileEngine.Tileset;
import byog.Core.World.Position;

public class Skeleton extends Mob {

    private static final long serialVersionUID = 1111111111117L;
    public Skeleton(Position p) {
        super(p, 10, 2, 20);
        setSprite(Tileset.SKELETON);
        name = "Skeleton";
        description = "A bony fellow.";
    }


}
