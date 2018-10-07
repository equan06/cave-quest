package byog.Core.Mobs;

import byog.TileEngine.Tileset;
import byog.Core.World.Position;

public class Goblin extends Mob {

    private static final long serialVersionUID = 1111111111118L;
    public Goblin(Position p) {
        super(p, 5, 1, 10);
        setSprite(Tileset.GOBLIN);
        name = "Goblin";
        description = "An ugly brute.";
    }

    public Goblin(Position p, int hp, int atk, int xpval) {
        super(p, hp, atk, xpval);
        setSprite(Tileset.GOBLIN);
        name = "Goblin";
        description = "An ugly brute.";
    }
}
