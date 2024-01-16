package agh.oop.model.objects;

import agh.oop.model.map.MapDirection;
import agh.oop.model.map.MapOptions;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.inheritance.Genome;
import agh.oop.model.objects.inheritance.Mutation;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Animal implements WorldElement {

    private static int initialEnergy;

    private final UUID animalId;
    private Vector2d position;
    private MapDirection direction = MapDirection.generate();
    private int energy;
    private final Genome genome;
    private final int copulateEnergy;//energy taken from parent and given to child
    private int lifeLength = 0;
    private int childrenCount = 0;
    private Optional<Integer> dayOfDeath = Optional.empty();

    public Animal(Vector2d position, int initialEnergy, Genome genome, int copulateEnergy) {
        this.position = position;
        this.energy = initialEnergy;
        this.genome = genome;
        this.animalId = UUID.randomUUID();
        this.copulateEnergy = copulateEnergy;
        Animal.initialEnergy=initialEnergy;
    }

    public UUID getId() {
        return animalId;
    }

    public Vector2d getPosition() {
        return new Vector2d(position.getX(), position.getY());
    }

    @Override
    public String getImage() {
        return "animal.png";
    }

    public MapDirection getDirection() {
        return direction;
    }

    public Optional<Integer> getDayOfDeath() {
        return dayOfDeath;
    }

    public void setDirection(MapDirection direction) {
        this.direction = direction;
    }

    public void setDayOfDeath(Optional<Integer> dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
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

    public Genome getGenome() {
        return new Genome(genome.getGeneList(), genome.getGenomeLength());
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void incrementChildrenCount(){ this.childrenCount += 1;}

    @Override
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public boolean isDead() {
        return energy <= 0;
    }

    public void eat(Plant plant) {
        energy+=plant.getEnergy();
    }

    public Animal reproduce(Animal other, Mutation mutation) {
        var percentage = (double) this.energy/(this.energy+other.energy);
        var newGenome = this.genome.generateNewGenome(mutation, other.genome, percentage);
        int initialEnergy = 2*copulateEnergy;
        this.setEnergy(this.getEnergy()-copulateEnergy);
        other.setEnergy(other.getEnergy()-copulateEnergy);
        return new Animal(position, initialEnergy, newGenome, copulateEnergy);
    }

    public void move(MapOptions options) {
        int active = genome.takeAnotherGene();
        direction = direction.shift(active);
        position = options.mover(position.add(direction.toVector()))
                .orElseGet(() -> {
                    direction = direction.shift(4);
                    return position;
                });
        energy--;
        lifeLength++;
    }

    public String getAnimalColor() {
        String color;
        if (energy<=0.5*initialEnergy) color = "White";
        else if (energy<=initialEnergy) color = "Red";
        else color = "Black";
        return color;
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
