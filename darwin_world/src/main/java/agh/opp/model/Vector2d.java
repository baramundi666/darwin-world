package agh.opp.model;

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

    public boolean precedes(Vector2d other) {
        return other.x >= this.x && other.y >= this.y;
    }

    public boolean follows(Vector2d other) {
        return other.x <= this.x && other.y <= this.y;
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(Math.max(this.x, other.x),Math.max(this.y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(Math.min(this.x, other.x),Math.min(this.y, other.y));
    }

    public Vector2d opposite() {
        return new Vector2d(-x, -y);
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
