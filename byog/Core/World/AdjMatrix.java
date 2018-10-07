package byog.Core.World;

import java.io.Serializable;

public class AdjMatrix implements Serializable {

    private static final long serialVersionUID = 1111111111116L;
    private int[][] adj;

    /**
     * AdjMatrix is an implementation of an nxn adjacency matrix, which
     * stores whether two nodes in a graph are connected. Each room
     * is a node, and a connection is defined by a corridor that directly
     * connects the centers of two rooms. If room i and j are connected,
     * then the i-jth entry is 1, and 0 otherwise.
     * @param n
     */
    public AdjMatrix(int n) {
        adj = new int[n][n];
    }

    /**
     * Depth First Search method to check whether the graph is connected.
     * By default, it starts at the first room in the list, and recursively checks
     * its connections. Then, it checks the connections' connections. If a room is
     * checked, then it is added to the traversed array. At the end, if traversed
     * is not full, then the graph is not connected.
     *
     * @source wikipedia, cs.yale.edu  */
    public void depthFirst(int curr, int[] traversed) {
        if (traversed[curr] == 1) {
            return;
        }
        traversed[curr] = 1;
        for (int j = 0; j < adj[curr].length; j++) {
            if (adj[curr][j] != 0) {
                depthFirst(j, traversed);
            }
        }
    }

    /**
     * Starting from the first room, perform a DFS to
     * find if the first room connects to all rooms, adding all traversed rooms
     * to the traversed array. Then, if all rooms have been traversed,
     * return -1. If not, return the index of a non-traversed room.*/
    public int isConnected() {
        int[] traversed = new int[adj.length];
        depthFirst(0, traversed);
        for (int i = 0; i < traversed.length; i++) {
            if (traversed[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Connect room1 and room2 by setting adj[1][2]
     * and adj[2][1] to 1.
     * Retrieves their ID nums which act as indices.
     * @param room1 first room object
     * @param room2 second room object
     * */
    public void addRooms(Room room1, Room room2) {
        int id1 = room1.getID();
        int id2 = room2.getID();
        adj[id1][id2] = 1;
        adj[id2][id1] = 1;
    }

    /**
     * If 2 rooms are connected, return true.
     * @param room1 first room
     * @param room2 second room
     * @return true if connected
     */
    public boolean checkConnected(Room room1, Room room2) {
        int id1 = room1.getID();
        int id2 = room2.getID();
        return (adj[id1][id2] == 1);
    }

    /**
     * Prints the adjacency matrix. For debugging.
     */
    public void printMatrix() {
        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj.length; j++) {
                System.out.print(adj[i][j] + " ");
            }
            System.out.println();
        }
    }
}
