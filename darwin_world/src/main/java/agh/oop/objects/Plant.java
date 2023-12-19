package agh.oop.objects;

import agh.oop.map.Vector2d;

public class Plant implements WorldElement {
    private final Vector2d position;
    private final int energy;

    public Plant(Vector2d position, int energy) {
        this.position = position;
        this.energy = energy;
    }
    public Vector2d getPosition() {
        return position;
    }
    public int getEnergy() {
        return energy;
    }
    @Override
    public boolean isAt(Vector2d position) {
        return false;
    }
}
