package byog.Core.World;

import byog.Core.Mobs.*;
import byog.Core.RandomUtils;
import byog.Core.HUD;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class World implements Serializable {
    private int HEIGHT;
    private int WIDTH;
    private Random random;
    private TETile[][] world;
    private AdjMatrix roomMatrix;
    private ArrayList<Room> roomList = new ArrayList<>();
    private Portal portal;
    private Campfire campfire;
    private FreezeStone freezeStone;
    private String[] mobTypes;
    private ArrayList<Mob> mobList = new ArrayList<>();
    private Player player;
    private int floorCount;
    private boolean bossLevel;

    private static final long serialVersionUID = 1111111111111L;

    public World(int width, int height, int fnum, Random r) {
        HEIGHT = height;
        WIDTH = width;
        world = new TETile[width][height];
        floorCount = fnum;
        random = r;
    }

    public TETile[][] getWorld() {
        return world;
    }

    public void setWorld(TETile[][] w) {
        world = w;
    }

    public Random getRandom() {
        return random;
    }

    public void setPlayer(Player p) {
        player = p;
    }

    public Player getPlayer() {
        return player;
    }
    
    public int getFloor() {
        return floorCount;
    }


    public void setTile(Position p, TETile t) {
        world[p.getX()][p.getY()] = t;
    }

    public TETile getTile(Position p) {
        return world[p.getX()][p.getY()];
    }

    public Portal getPortal() {
        return portal;
    }

    public Campfire getCampfire() {
        return campfire;
    }

    public FreezeStone getFreezeStone() { return freezeStone; }

    public Position getPortalPos() {
        return portal.getPos();
    }

    public Position getCampfirePos() {
        if (campfire != null) {
            return campfire.getPos();
        } else {
            return new Position(-1, -1);
        }

    }


    /** Developer command to display objects or not. */
    public void toggleWorldObjectsVisible() {
        portal.toggleVisible();
        portal.spawn(world);
        if (campfire != null) {
            campfire.toggleVisible();
            campfire.spawn(world);
        }
        if (freezeStone != null) {
            freezeStone.toggleVisible();
            freezeStone.spawn(world);
        }
    }

    public void toggleMobsVisible() {
        for (Mob m : mobList) {
            m.toggleVisible();
            m.spawnInWorld(world);
        }
    }

    public void destroyCampfire() {
        campfire.burnOut();
        campfire.show(world);
        campfire = null;
    }


    public void setRandom(Random r) {
        random = r;
    }

    public String getTileDescription(int x, int y) {
        TETile res = world[x][y];
        return res.description();
    }

    public void generateWorld() {
        bossLevel = false;
        roomMatrix = null;
        mobList = new ArrayList<>();
        roomList = new ArrayList<>();
        Room.resetIDCount();
        fillBlankWorld();
        addRandomRooms();
        connectAll();
        drawWalls();
        drawFloorTextures();
        spawnWorldObjects();
    }

    public void generateBossWorld() {
        bossLevel = true;
        roomMatrix = null;
        mobList = new ArrayList<>();
        roomList = new ArrayList<>();
        roomMatrix = null;
        mobList = new ArrayList<>();
        roomList = new ArrayList<>();
        Room.resetIDCount();
        fillBlankWorld();
        addBossRoom();
        drawWalls();
    }

    public void addBossRoom() {
        Room bossRoom = new BossRoom(new Position(WIDTH / 2 - 5, HEIGHT / 2 - 5), random);
        roomList.add(bossRoom);
        bossRoom.draw(world);
        Corridor c1 = new Corridor(new Position(5, 21), 70, 1, random);
        Corridor c2 = new Corridor(new Position( 30, 10), 1, 11, random);
        Corridor c3 = new Corridor(new Position(54, 10), 1, 11, random);
        Corridor c4 = new Corridor(new Position(30, 10), 24, 1, random);
        c1.draw(world);
        c2.draw(world);
        c3.draw(world);
        c4.draw(world);

        portal = new Portal(new Position(74, 21));
        portal.spawn(world);
    }

    /**
     * Return a random room from roomList. Used for instantiating random objects in random rooms.
     * @return room in roomList */
    public Room getRandomRoom() {
        return roomList.get(RandomUtils.uniform(random, roomList.size()));
    }


    /**
     * Adds a room to world at Position pos. It will generate a room first,
     * then discard it if it overlaps any existing rooms.
     * If it doesn't overlap, we add it to the list of rooms, and
     * draw it on the world.
     * @param pos the Position of the bottom-left corner of the room */
    public void addRoom(Position pos) {
        Room room = new Room(pos, random);
        for (Room r : roomList) {
            if (room.overlaps(r)) {
                return;
            }
        }
        roomList.add(room);
        room.draw(world);
    }

    /** Fill the entire world with NOTHING tiles. Should only be used at world creation. */
    public void fillBlankWorld() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Adds rooms to random positions starting from (1,1) to 10 units
     * below (WIDTH, HEIGHT) to prevent rooms from being spawned in corners. */
    public void addRandomRooms() {
        for (int i = 0; i < HEIGHT + WIDTH; i++) {
            int randX = RandomUtils.uniform(random, 1, WIDTH - 10);
            int randY = RandomUtils.uniform(random, 1, HEIGHT - 10);
            addRoom(new Position(randX, randY));
        }
        roomMatrix = new AdjMatrix(roomList.size());
    }

    /**
     * Connects all rooms in the roomList. First, for each room,
     * connect it to its closest room 4/5 of the time, or a random room 1/5
     * of the time. This is simply to make more interesting layouts. Then, check
     * the adjacency matrix to see if all rooms are connected. If not, iteratively
     * connect the first room to any unconnected rooms until all rooms are connected.
     *
     * */
    public void connectAll() {
        for (Room r : roomList) {
            int randNum = RandomUtils.uniform(random, 0, 5);
            if (randNum < 4) {
                r.connectTo(roomMatrix, r.findClosest(roomList), world);
            } else {
                r.connectTo(roomMatrix, r.findRandom(roomList), world);
            }
        }
        int unconnected = roomMatrix.isConnected();
        while (unconnected != -1) {
            roomList.get(0).connectTo(roomMatrix, roomList.get(unconnected), world);
            unconnected = roomMatrix.isConnected();
        }
    }

    /**
     * Checks each world tile to see if it is first NOTHING, and then adjacent to a FLOOR tile.
     * If so, then place a WALL tile. Essentially, the floors are drawn first, and then the
     * walls are added. For efficiency's sake, we forgo the usage of Position objects here.
     * @param x x position
     * @param y y position
     * @return true if tile (x,y) is next to a FLOOR tile
     */
    public boolean isTileNextToRoom(int x, int y) {
        boolean roomFound = false;
        if (!world[x][y].equals(Tileset.NOTHING)) {
            return false;
        }
        for (int i = Math.max(x - 1, 0); i <= x + 1; i++) {
            for (int j = Math.max(y - 1, 0); j <= y + 1; j++) {
                if (world[i][j].equals(Tileset.FLOOR)) {
                    roomFound = true;
                }
            }
        }
        return roomFound;
    }

    /**
     * Draws WALL tile on every NOTHING tile that is adjacent to a FLOOR tile.
     */
    public void drawWalls() {
        for (int i = 0; i < WIDTH - 1; i++) {
            for (int j = 0; j < HEIGHT - 1; j++) {
                if (isTileNextToRoom(i, j)) {
                    int wallColor = RandomUtils.uniform(random, 0, 4);
                    TETile wall = Tileset.WALL1;
                    switch (wallColor) {
                        case 1: wall = Tileset.WALL2;
                            break;
                        case 2: wall = Tileset.WALL3;
                            break;
                        case 3: wall = Tileset.WALL4;
                            break;
                        default: break;
                    }
                    world[i][j] = wall;
                }
            }
        }
    }

    public void drawFloorTextures() {
        for (Room r : roomList) {
            int rng = RandomUtils.uniform(random, 10);
            if (rng > 1) {
                drawGrass(r);
            }
        }
    }

    public void drawGrass(Room r) {
        Position botLeft = r.getPos();
        Position topRight = r.getMaxpos();
        int x1 = botLeft.getX();
        int y1 = botLeft.getY();
        int x2 = topRight.getX();
        int y2 = topRight.getY();
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                int rng = RandomUtils.uniform(random, 5);
                if (rng == 0 || rng == 1) {
                    world[i][j] = Tileset.GRASS;
                } else if (rng == 2) {
                    world[i][j] = Tileset.FLOWER;
                }
            }
        }
    }


    public void spawnWorldObjects() {
        Room portalRoom = findObjectRoom();
        portalRoom.setOccupied();
        portal = new Portal(portalRoom.getCenterpos());
        portal.spawn(world);

        Room campfireRoom = findObjectRoom();
        campfireRoom.setOccupied();
        campfire = new Campfire(campfireRoom.getCenterpos());
        campfire.spawn(world);

        int rng = RandomUtils.uniform(random, 10);
        if (rng < 2) {
            Room freezeStoneRoom = findObjectRoom();
            freezeStoneRoom.setOccupied();
            freezeStone = new FreezeStone(freezeStoneRoom.getCenterpos());
            freezeStone.spawn(world);
        }
    }

    public Room findObjectRoom() {
        Room randomRoom = getRandomRoom();
        while (randomRoom.isPortalSpawnInvalid() || randomRoom.isOccupied()) {
            randomRoom = getRandomRoom();
        }
        return randomRoom;
    }

    public Room findEmptyRoom() {
        Room randomRoom = getRandomRoom();
        while (randomRoom.isOccupied()) {
            randomRoom = getRandomRoom();
        }
        return randomRoom;
    }

    public int numValidEmptyRooms() {
        int count = 0;
        for (Room r : roomList) {
            if (!r.isOccupied()) {
                count++;
            }
        }
        return count;
    }

    public void spawnMobs(int numMobs) {
        if (numMobs > numValidEmptyRooms()) {
            numMobs = numValidEmptyRooms();
        }

        setMobTypes();
        for (int i = 0; i < numMobs; i++) {
            Room mobRoom = findEmptyRoom();
            mobRoom.setOccupied();

            String mobType = getRandomMobType();
            Mob mob;
            switch (mobType) {
                case "Skeleton":
                    mob = new Skeleton(mobRoom.getCenterpos());
                    break;
                case "Rock Golem":
                    mob = new RockGolem(mobRoom.getCenterpos());
                    break;
                case "Demon":
                    mob = new Demon(mobRoom.getCenterpos());
                    break;
                default:
                    mob = new Goblin(mobRoom.getCenterpos());
                    break;
            }
            mob.isCloseToPlayer(this);
            mob.spawnInWorld(world);
            mobList.add(mob);
        }
    }

    public void spawnBossMobs() {
        setMobTypes();
        int rng = RandomUtils.uniform(random, 0, 2);
        if (floorCount == 11) {
            spawnMiniBoss(true);
        } else if (floorCount == 8) {
            spawnMobParty();
        } else if (floorCount == 5) {
            spawnMiniBoss(false);
        }
    }

    public void spawnMobParty() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                String mobType = getRandomMobType();
                Mob mob;
                Position pos = new Position(40 + i, 18 + j);
                switch (mobType) {
                    case "Skeleton":
                        mob = new Skeleton(pos);
                        break;
                    case "Rock Golem":
                        mob = new RockGolem(pos);
                        break;
                    case "Demon":
                        mob = new Demon(pos);
                    default:
                        mob = new Goblin(pos, 10, 3, 20);
                        break;
                }
                mob.isCloseToPlayer(this);
                mob.spawnInWorld(world);
                mobList.add(mob);
            }
        }
    }

    public void spawnMiniBoss(boolean finalBoss) {
        Position pos = new Position(42, 21);
        Mob mob;
        if (finalBoss) {
            mob = new Archmage(pos);
        } else {
            mob = new Archdemon(pos);
        }
        mobList.add(mob);
        mob.spawnInWorld(world);
    }


    public Mob getMobFromPos(Position p) {
        for (int i = 0; i < mobList.size(); i++) {
            Mob mob = mobList.get(i);
            if (mob.getPos().equals(p)) {
                return mob;
            }
        }
        return null;
    }

    public void removeMob(Mob mob) {
        mobList.remove(mob);
        replaceMobWithFloor(mob);
    }

    public void replaceMobWithFloor(Mob mob) {
        Position mobPos = mob.getPos();
        setTile(mobPos, mob.getHiddenSprite());
    }

    /**
     * Iterate through mobList and have each mob take a turn if
     * within distance of player. Mobs will move toward player
     * in shortest dx/dy distance.
     * */
    public String mobTurns(HUD hud) {
        String mobTurnRes;
        for (int i = 0; i < mobList.size(); i++) {
            Mob mob = mobList.get(i);
            if (mob.isCloseToPlayer(this)) {
/*                int randMove = RandomUtils.uniform(random, 4);
                char c;
                switch (randMove) {
                    case 0: c = '0';
                        break;
                    case 1: c = '1';
                        break;
                    case 2: c = '2';
                        break;
                    default: c = '3';
                        break;
                }*/
                int dx = player.getPos().getX() - mob.getPos().getX();
                int dy = player.getPos().getY() - mob.getPos().getY();
                mobTurnRes = mob.moveTowardPlayer(dx, dy, this);
                if (hud != null && mobTurnRes.length() > 0) {
                    hud.addNewMsg(mobTurnRes);
                }
            }
        }
        return "";
    }


    /**
     * Spawns the player and mobs in the center of a random room that is unoccupied.
     * If render is true, then HUD is created and the world is rendered as well.
     * If mob is true, spawn mobs as well. */
    public Player spawnPlayerAndMobs(Player p, boolean mobs) {
        if (bossLevel) {
            if (p == null) {
                player = new Player(new Position(5, 21));
            } else {
                player = p;
                player.setPos(new Position(5, 21));
            }
            player.spawnInWorld(world);
        } else {
            Room randomRoom = getRandomRoom();
            while (randomRoom.isOccupied()) {
                randomRoom = getRandomRoom();
            }
            randomRoom.setOccupied();
            if (p == null) {
                player = new Player(randomRoom.getCenterpos());
            } else {
                player = p;
                player.setPos(randomRoom.getCenterpos());
            }
            player.spawnInWorld(world);
            if (mobs) {
                spawnMobs(Math.max(4, floorCount * 2));
            }
        }


        return player;
    }



    public boolean mobsInAtkRange(Mob mob1, Mob mob2) {
        Position p1 = mob1.getPos();
        Position p2 = mob2.getPos();
        return p1.validAtkPos(p2);
    }

    public void setMobTypes() {
        mobTypes = MobSet.randomMobArray(floorCount);
    }

    public String getRandomMobType() {
        int randNum = RandomUtils.uniform(random, 0, mobTypes.length);
        return mobTypes[randNum];
    }

    /** Direct room adder. for TESTING purposes only! */
    public void addRoom(Room room) {
        roomList.add(room);
        room.draw(world);
    }

    /** Direct roomMatrix creaiton. for TESTING. */
    public void createRoomMatrix() {
        roomMatrix = new AdjMatrix(roomList.size());
    }
}
