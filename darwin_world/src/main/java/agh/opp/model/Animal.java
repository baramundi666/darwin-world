package agh.opp.model;

import java.util.Map;

public class Animal implements WorldElement{

    private Vector2d position;
    private MapDirection direction = MapDirection.N;
    private int energy;
    private final Genome genome;


    public Animal(Vector2d position, int initialEnergy, Genome genome) {
        this.position = position;
        this.energy = initialEnergy;
        this.genome = genome;
    }

    public Vector2d getPosition() {
        return new Vector2d(position.getX(), position.getY());
    }

    public MapDirection getDirection() {
        return direction;
    }

    public int getEnergy() {
        return energy;
    }

    public Genome getGenome() {
        // to be changed - rn allows to modify genome from outside
        return genome;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.position==position;
    }

    public void eat(Plant plant) {
        energy+=plant.getEnergy();
    }

    public void reproduce(Animal animal) {

    }

    public void move() {
        direction = direction.shift(genome.getActiveGene());
        genome.nextGene();
        // naiwne, bez rozpatrzania wlasciwosci mapy
        position = position.add(direction.toVector());
        energy--;
    }
}
