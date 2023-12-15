package agh.opp.model;

public class Plant implements WorldElement{

    private Vector2d position;
    private int energy;


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