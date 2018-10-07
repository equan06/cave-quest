package byog.Core.World;

import java.io.Serializable;

/** Simple Position object that holds an object's x position and y position
 *  at their bottom left corner. */
public class Position implements Serializable {

    private static final long serialVersionUID = 1111111111115L;
    private int x = 0;
    private int y = 0;

    public Position() {
        this.x = 0;
        this.y = 0;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Position p) {
        return this.x == p.getX() && this.y == p.getY();
    }

    /** Return euclidean dist between two positions. */
    public int diff(Position pos2) {
        int dx = pos2.x - x;
        int dy = pos2.y - y;
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    /** Updates the current position by newX or newY. */
    public void updateX(int newX) {
        this.x += newX;
    }

    public void updateY(int newY) {
        this.y += newY;
    }

    public void updateXY(int newX, int newY) {
        this.x += newX;
        this.y += newY;
    }

    public int dx(Position pos2) {
        return pos2.getX() - x;
    }

    public int dy(Position pos2) {
        return pos2.getY() - y;
    }

    public Position makeCopy() {
        return new Position(this.x, this.y);
    }

    public boolean validAtkPos(Position p2) {
        if (getX() == p2.getX() && getY() + 1 == p2.getY()) {
            return true;
        } else if (getX() == p2.getX() && getY() - 1 == p2.getY()) {
            return true;
        } else if (getY() == p2.getY() && getX() + 1 == p2.getX()) {
            return true;
        } else if (getY() == p2.getY() && getX() - 1 == p2.getX()) {
            return true;
        }
        return false;
    }

    public boolean isNull() {
        return x == -1;
    }

    public String toString() {
        return "x: " + this.x + " y: " + this.y;
    }
}
