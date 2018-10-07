package byog.Core.Mobs;

import byog.Core.World.Position;
import byog.Core.World.World;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Archmage extends Mob {
    public Archmage(Position p) {
        super(p, 20, 4, 1000);
        setSprite(Tileset.MAGE);
        name = "Archmage";
        description = "A wizard.";
        setFreezable(false);
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
        return (diff <= 4 && diff >= 2);
    }
}
