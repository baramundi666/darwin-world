package agh.oop.model.objects;

import agh.oop.model.map.MapDirection;
import agh.oop.model.map.MoveOptions;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.inheritance.Genome;

public class Animal implements WorldElement {
    private Vector2d position;
    private MapDirection direction;
    private int energy;
    private final Genome genome;
    private int lifeLength = 0;
    private int childrenCount = 0;

    public Animal(Vector2d position, int initialEnergy, Genome genome, MapDirection direction) {
        this.position = position;
        this.energy = initialEnergy;
        this.genome = genome;
        this.direction = direction;
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
        return new Genome(genome.getGeneList(), genome.getGenomeLength());
    }
    public void incrementLifeLength() {
        this.lifeLength += 1;
    }
    public void incrementChildrenCount(){ this.childrenCount += 1;}
    @Override
    public boolean isAt(Vector2d position) {
        return this.position==position;
    }
    public void eat(Plant plant) {
        energy+=plant.getEnergy();
    }
    public void reproduce(Animal animal) {
        //to implement
    }
    public void move(MoveOptions options) {
        direction = direction.shift(genome.getActiveGene());
        position = options.mover(position.add(direction.toVector()))
                .orElseGet(() -> {
                    direction = direction.shift(4);
                    return position;
                });
        genome.nextGene();
        energy--;
    }
}
//divide into animal, animalController???
