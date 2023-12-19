package agh.oop.model.objects;

import agh.oop.model.map.MapDirection;
import agh.oop.model.map.MoveOptions;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.inheritance.Genome;
import java.util.Objects;
import java.util.UUID;

public class Animal implements WorldElement {

    private final UUID animalId;
    private Vector2d position;
    private MapDirection direction= MapDirection.N;
    private int energy;
    private final Genome genome;

    private final int copulateEnergy;
    private int lifeLength = 0;
    private int childrenCount = 0;


    public Animal(Vector2d position, int initialEnergy, Genome genome, int copulateEnergy) {
        this.position = position;
        this.energy = initialEnergy;
        this.genome = genome;
        this.animalId = UUID.randomUUID();
        this.copulateEnergy = copulateEnergy;
    }

    public UUID getId() {
        return animalId;

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

    public int getLifeLength() {
        return lifeLength;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public Genome getGenome() {
        return new Genome(genome.getGeneList(), genome.getGenomeLength());
    }
    public void incrementChildrenCount(){ this.childrenCount += 1;}
    @Override
    public boolean isAt(Vector2d position) {
        return this.position==position;
    }
    public void eat(Plant plant) {
        energy+=plant.getEnergy();
    }

    public Animal reproduce(Animal other) {
        Genome newGenome = this.getGenome().merge(other.getGenome(), this.energy, other.energy);
        int initialEnergy = 2*copulateEnergy;
        this.setEnergy(this.getEnergy()-copulateEnergy);
        other.setEnergy(other.getEnergy()-copulateEnergy);
        return new Animal(position, initialEnergy, newGenome, copulateEnergy);
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
        lifeLength++;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Animal))
            return false;
        Animal that = (Animal) other;
        return that.getId() == this.getId();
    }
    @Override
    public final int hashCode() {
        return Objects.hash(animalId);
    }
}
//divide into animal, animalController???
