package byog.Core.Mobs;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.Core.World.Position;
import byog.Core.World.World;

import java.io.Serializable;

public abstract class Mob implements Being, Serializable {

    protected Position pos;
    private TETile sprite;
    private TETile hiddenSprite = TETile.darkenTile(Tileset.FLOOR);
    private TETile hiddenSpriteRegular = Tileset.FLOOR;
    protected int xp = 0;
    protected int health = 0;
    protected int maxhealth = 0;
    protected int atk = 1;
    protected String name;
    protected String description;
    private boolean alive = true;
    private boolean close = false;
    private boolean visible = false; // used only for dev mode
    private boolean poisoned = false;
    private boolean frozen = false;
    private int frozenDuration = 0;
    private boolean hasFreeze = false;
    private boolean freezable = true;


    public Mob(Position p) {
        pos = p;
    }

    public Mob(Position p, int maxhp, int attack) {
        pos = p;
        health = maxhp;
        maxhealth = maxhp;
        atk = attack;
    }

    public Mob(Position p, int maxhp, int attack, int xpval) {
        pos = p;
        health = maxhp;
        maxhealth = maxhp;
        atk = attack;
        xp = xpval;
    }


    public void setFreezable(boolean val) {
        freezable = val;
    }
    public Position getPos() {
        return pos;
    }

    public void setPos(Position p) {
        pos = p;
    }

    public void toggleVisible() {
        visible = !visible;
    }

    public void toggleFreezeAbility() {
        hasFreeze = !hasFreeze;
    }

    public boolean checkHasFreeze() {
        return hasFreeze;
    }

    public int getHealth() {
        return health;
    }

    public void modifyHealth(int x) {
        health -= x;
    }

    public void setHealth(int x) {
        health = x;
    }

    public int getMaxhealth() {
        return maxhealth;
    }

    public String getHealthString() {
        if (health < 10) {
            return " " + health + "/" + maxhealth; // space before single digit health
        }
        return health + "/" + maxhealth;
    }

    public int getAtk() {
        return atk;
    }

    public void updateAtk() {
        atk += 1;
    }

    public int getXp() {
        return xp;
    }

    public void setSprite(TETile t) {
        sprite = t;
    }

    public TETile getSprite() {
        return sprite;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setHiddenSprite(TETile t) {
        hiddenSprite = t;
    }

    public TETile getHiddenSprite() {
        return hiddenSprite;
    }

    public void die() {
        alive = false;
    }

    public boolean isDead() {
        return !alive;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public String freeze() {
        if (freezable) {
            frozen = true;
            frozenDuration = 2;
            return name + " is frozen!";
        }
        return "";
    }

    public String unfreeze() {
        frozen = false;
        frozenDuration = 0;
        return name + " has defrosted!";
    }

    public boolean isPoisoned() {
        return poisoned;
    }

    public void spawnInWorld(TETile[][] world) {
        if (close || visible || this.name.equals("You")) {
            world[pos.getX()][pos.getY()] = sprite;
        } else {
            world[pos.getX()][pos.getY()] = hiddenSprite;
        }
    }

    public String attack(Mob mob, World w) {
        mob.modifyHealth(atk);
        String res = name + " hit " + mob.getName() + " for " + atk + " dmg.";
        if (mob.getHealth() <= 0) {
            mob.setHealth(0);
            mob.die();
            w.removeMob(mob);
            res = name + " defeated " + mob.getName() + ".";
        } else if (hasFreeze) {
            res += " " + mob.freeze();
        }
        return res;
    }


    public boolean isNextToPlayer(World w) {
        return w.mobsInAtkRange(this, w.getPlayer());
    }

    public boolean isCloseToPlayer(World w) {
        Player p = w.getPlayer();
        int dist = p.getPos().diff(pos);
        if (dist <= 7) {
            close = true;
        } else {
            close = false;
        }
        spawnInWorld(w.getWorld());
        return close;
    }


    public String move(char input, World w) {
        if (isNextToPlayer(w)) {
            Mob player = w.getPlayer();
            return attack(player, w);
        } else {
            Position newPos = pos.makeCopy();
            switch (input) {
                case '0':
                    newPos.updateY(1);
                    break;
                case '1':
                    newPos.updateX(-1);
                    break;
                case '2':
                    newPos.updateY(-1);
                    break;
                default:
                    newPos.updateX(1);
                    break;
            }

            TETile nextTile = w.getTile(newPos);

            if (nextTile.equals(Tileset.FLOOR) || nextTile.equals(Tileset.GRASS)) {
                if (visible) {
                    w.setTile(pos, hiddenSpriteRegular);
                } else {
                    w.setTile(pos, hiddenSprite);
                }
                w.setTile(newPos, sprite);
                pos = newPos;
            }
            return "";
        }
    }

    public String frozenMove() {
        if (frozenDuration == 0) {
            return unfreeze();
        } else if (frozen) {
            frozenDuration -= 1;
            return name + " is frozen; could not move.";
        }
        return "";
    }

    public String moveTowardPlayer(int dx, int dy, World w) {
        if (frozen) {
            return frozenMove();
        }
        if (isNextToPlayer(w)) {
            Mob player = w.getPlayer();
            return attack(player, w);

        }
        boolean xDir;
        Position newPos = pos.makeCopy();
        if ((Math.abs(dx) <= Math.abs(dy) && dx != 0) || dy == 0) { // closer in x direction
            if (dx < 0) {
                newPos.updateX(-1);
            } else {
                newPos.updateX(1);
            }
            xDir = true;
        } else {
            if (dy < 0) {
                newPos.updateY(-1);
            } else {
                newPos.updateY(1);
            }
            xDir = false;
        }

        TETile nextTile = w.getTile(newPos);
        if (nextTile.equals(Tileset.WALL) || nextTile.equals(Tileset.PORTAL) || nextTile.equals(Tileset.FREEZE)) {
            xDir = !xDir;
            newPos = pos.makeCopy();
            if (xDir) {
                if (dx < 0) {
                    newPos.updateX(-1);
                } else {
                    newPos.updateX(1);
                }
            } else {
                if (dy < 0) {
                    newPos.updateY(-1);
                } else {
                    newPos.updateY(1);
                }
            }
        }

        nextTile = w.getTile(newPos);
        if (nextTile.equals(Tileset.FLOOR) || nextTile.equals(Tileset.GRASS) || nextTile.equals(Tileset.FLOWER)) {

            if (visible) {
                w.setTile(pos, hiddenSpriteRegular);
            } else {
                w.setTile(pos, hiddenSprite);
            }
            hiddenSpriteRegular = nextTile;
            hiddenSprite = TETile.darkenTile(hiddenSpriteRegular);
            w.setTile(newPos, sprite);
            pos = newPos;
        }
        return "";
    }



    public String toString() {
        return description;
    }
}
