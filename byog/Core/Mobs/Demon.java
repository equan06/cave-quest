package byog.Core.Mobs;

import byog.Core.World.Position;
import byog.Core.World.World;
import byog.TileEngine.Tileset;

public class Demon extends Mob {
    public Demon(Position p) {
        super(p, 15, 3, 40);
        setSprite(Tileset.DEMON);
        name = "Demon";
        description = "A demon.";
    }


    @Override
    public String moveTowardPlayer(int dx, int dy, World w) {
        Player p = w.getPlayer();
        if (playerInRange(p)) {
            return attack(p, w);
        } else {
            return super.moveTowardPlayer(dx, dy, w);
        }
    }

    public boolean playerInRange(Player p) {
        int diff = getPos().diff(p.getPos());
        return (diff <= 2);
    }
}
