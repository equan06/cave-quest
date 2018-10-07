package byog.Core.Mobs;

import byog.Core.World.WorldObject;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.Core.World.Position;
import byog.Core.World.World;

public class Player extends Mob {

    private static final long serialVersionUID = 111111111121L;
    private boolean togglePos = false;
    private int level = 1;
    private int currentXp = 0;
    private static final int[] LEVELS = {10, 20, 40, 60, 100, 140, 180, 240, 300, 360, 420};
    private int killCount = 0;

    public Player(Position p) {
        super(p, 10, 2);
        setSprite(Tileset.PLAYER);
        name = "You";
        description = "The almighty Player Character.";
    }

    public Player(Position p, int maxhealth, int atk, boolean toggle) {
        super(p, maxhealth, atk);
        togglePos = toggle;
        name = "You";
        description = "The almighty Player Character.";
    }


    public boolean isTogglePos() {
        return togglePos;
    }

    public int getKillcount() {
        return killCount;
    }
    public int getLevel() {
        return level;
    }

    public int getXp() {
        return currentXp;
    }

    public String getXpString() {
        return currentXp + "/" + LEVELS[level - 1];
    }
    /** Creates a copy of a Player at new position p.*/
    public Player copyOf(Position p) {
        return new Player(p, getMaxhealth(), getAtk(), togglePos);
    }

    public String closeToWorldObjects(World w) {
        String res = "";
        WorldObject p = w.getPortal();
        WorldObject c = w.getCampfire();
        WorldObject f = w.getFreezeStone();

        if (p != null && p.getPos().diff(pos) <= 5 && !p.isVisible()) {
            p.setFound();
            p.show(w.getWorld());
            res = "portal";
        }
        if (c != null && c.getPos().diff(pos) <= 5 && !c.isVisible()) {
            c.setFound();
            c.show(w.getWorld());
            res = "campfire";
        }
        if (f != null && f.getPos().diff(pos) <= 5 && !f.isVisible()) {
            f.setFound();
            f.show(w.getWorld());
            res = "freeze";
        }
        return res;
    }

    @Override
    public String move(char input, World w) {
        Position newPos = pos.makeCopy(); // create a copy of curr pos, and modify the copy.

        if (input == 'w' || input == 'W') {
            newPos.updateY(1);
        } else if (input == 'a' || input == 'A') {
            newPos.updateX(-1);
        } else if (input == 's' || input == 'S') {
            newPos.updateY(-1);
        } else if (input == 'd' || input == 'D') {
            newPos.updateX(1);
        } else if (input == '~') {
            togglePos = !togglePos;
        }

        TETile nextTile = w.getTile(newPos); // check newPos

        if (nextTile.equals(Tileset.WALL)) {
            return ""; //do nothing
        } else if (nextTile.equals(Tileset.GOBLIN)
                || nextTile.equals(Tileset.SKELETON)
                || nextTile.equals(Tileset.ROCKGOLEM)
                || nextTile.equals(Tileset.DEMON)
                || nextTile.equals(Tileset.MAGE)
                || nextTile.equals(Tileset.ARCHDEMON)) { // ATTACK SUITE
            Mob mob = w.getMobFromPos(newPos); // get mob from newPos
            String moveRes = attack(mob, w);
            if (mob.isDead()) {
                moveRes += " " + gainXp(mob);
                killCount += 1;
            }
            return moveRes;
        } else if (nextTile.equals(Tileset.PORTAL)) {
            return "create";
        } else if (nextTile.equals(Tileset.CAMPFIRE)) {
            health += 5;
            return "rest";
        } else if (nextTile.equals(Tileset.FREEZE)) {
            toggleFreezeAbility();
            return "freeze";
        } else { // move from pos to newPos
            w.setTile(pos, getHiddenSprite()); // set curr to saved tile
            setHiddenSprite(nextTile); // save the newPos tile
            w.setTile(newPos, getSprite()); // set newPos to sprite
            pos = newPos; // update player pos
        }
        return "";
    }
    
    public String gainXp(Mob mob) {
        currentXp += mob.getXp();
        if (currentXp >= LEVELS[level - 1]) {
            return levelUp(LEVELS[level - 1]);
        }
        return "";
    }

    /**
     * Regen either health or maxHealth, depending on
     * which is larger (can boost health above maxhealth,
     * in which case the boosted health is kept).
     */
    public void restoreHealth() {
        health = Math.max(health, maxhealth);
    }

    public String levelUp(int levelXp) {
        level += 1;
        maxhealth += 2;
        restoreHealth();
        atk += 1;
        currentXp = currentXp - levelXp; // overflow xp
        if (currentXp >= LEVELS[level - 1]) { // if overflow xp still higher than next level, level up again
            return levelUp(LEVELS[level - 1]);
        }
        return "You grow stronger.";
    }
}
