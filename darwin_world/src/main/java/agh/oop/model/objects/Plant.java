package agh.oop.model.objects;

import agh.oop.model.map.Vector2d;

public class Plant implements WorldElement {
    private final Vector2d position;
    private final int energy;
    private final boolean isPoisonous;

    public Plant(Vector2d position, int energy, boolean isPoisonous) {
        this.position = position;
        this.energy = energy;
        this.isPoisonous = isPoisonous;
    }
    @Override
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
