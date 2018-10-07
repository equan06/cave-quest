package byog.Core.Mobs;

import byog.Core.World.Position;
import byog.TileEngine.Tileset;

public class Archdemon extends Mob {
    public Archdemon(Position p) {
        super(p, 10, 3, 200);
        setSprite(Tileset.ARCHDEMON);
        name = "Archdemon";
        description = "run.";
        setFreezable(false);
    }
}
