package agh.oop.map;

import java.util.Objects;

public class Vector2d {

    private final int x;
    private final int y;


    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "("+ x +", "+ y +")";
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public Vector2d mirror() {
        // currently assuming world map is symmetrical along the Y axis
        // to be implemented
        return new Vector2d(-this.x,this.y);
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return that.getX() == this.x && that.getY() == this.y;
    }
    @Override
    public final int hashCode() {
        return Objects.hash(x, y);
    }
}
