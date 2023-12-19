package agh.opp.model;

public enum MapDirection {
    N, NE, E, SE, S, SW, W, NW;

    public Vector2d toVector() {
        return switch(this) {
            case N -> new Vector2d(0,1);
            case NE -> new Vector2d(1,1);
            case E -> new Vector2d(1,0);
            case SE -> new Vector2d(1,-1);
            case S -> new Vector2d(0,-1);
            case SW -> new Vector2d(-1,-1);
            case W -> new Vector2d(-1,0);
            case NW -> new Vector2d(-1,1);
        };
    }

    public MapDirection shift(int gene) {
        int currentDirection = switch(this) {
            case N -> 0;
            case NE -> 1;
            case E -> 2;
            case SE -> 3;
            case S -> 4;
            case SW -> 5;
            case W -> 6;
            case NW -> 7;
        };
        return switch((currentDirection+gene)%8) {
            case 0 -> N;
            case 1 -> NE;
            case 2 -> E;
            case 3 -> SE;
            case 4 -> S;
            case 5 -> SW;
            case 6 -> W;
            case 7 -> NW;
            default -> throw new IllegalStateException("Unexpected value: " + (currentDirection + gene) % 8);
        };
    }
}
