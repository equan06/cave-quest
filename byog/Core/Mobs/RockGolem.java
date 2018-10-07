package byog.Core.Mobs;

import byog.TileEngine.Tileset;
import byog.Core.World.Position;

public class RockGolem extends Mob {

    private static final long serialVersionUID = 1111111111122L;
    public RockGolem(Position p) {
        super(p, 20, 2, 40);
        setSprite(Tileset.ROCKGOLEM);
        name = "Rock Golem";
        description = "A massive rock monster.";
    }
}
