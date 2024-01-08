package agh.oop.simulation.day;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.inheritance.Mutation;
import agh.oop.simulation.DataHolder;
import agh.oop.simulation.spawner.AbstractSpawner;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractSimulationDay {

    protected final Earth earth;
    protected final HashSet<Animal> animals;//statistics purpose
    protected final HashSet<Vector2d> notGrownFields;
    protected final int reproduceEnergy;
    protected final int newPlantNumber;
    protected final int plantEnergy;
    protected final AbstractSpawner spawner;
    protected final Mutation mutation;


    public AbstractSimulationDay(Earth earth, HashSet<Animal> animals,
                                 HashSet<Vector2d> notGrownFields,
                                 AbstractSpawner spawner, Mutation mutation, DataHolder simulationParameters) {
        this.earth = earth;
        this.animals = animals;
        this.notGrownFields = notGrownFields;
        this.reproduceEnergy = simulationParameters.reproduceEnergy();
        this.newPlantNumber = simulationParameters.newPlantNumber();
        this.plantEnergy = simulationParameters.plantEnergy();
        this.spawner = spawner;
        this.mutation = mutation;
    }

    public void simulateOneDay(){
            removeDeadAnimals();
            moveAnimals();
            animalsEat();
            reproduceAnimals();
            spawner.spawnPlants();
    }

    protected abstract void moveAnimals();

    protected void animalsEat(){
        var animalMap = earth.getAnimals();
        var plantMap = earth.getPlants();
        var toBeEaten = new HashMap<Vector2d, Animal>();

        for (Vector2d position : animalMap.keySet()) {
            if (plantMap.containsKey(position)) {
                List<Animal> strongest = conflict(animalMap.get(position));
                toBeEaten.put(position, strongest.get(0));
                notGrownFields.add(position);
            }
        }
        for (Vector2d position : toBeEaten.keySet()) {
            toBeEaten.get(position).eat(plantMap.get(position));
            earth.removePlant(plantMap.get(position));
        }
        spawner.setNotGrownFields(notGrownFields);
    }

    protected void removeDeadAnimals(){
        var animalMap = earth.getAnimals();
        var toRemove = new LinkedList<Animal>();
        if(!animalMap.isEmpty()) {
            for (Vector2d position : animalMap.keySet()) {
                var animalList = animalMap.get(position);
                for (Animal animal : animalList) {
                    if (animal.getEnergy() <= 0) {
                        toRemove.add(animal);
                        animals.remove(animal);
                    }
                }
            }
        }
        for (Animal animal : toRemove) {
            earth.removeAnimal(animal);
        }
    }

    protected void reproduceAnimals(){
        var animalMap = earth.getAnimals();
        var toPlace = new LinkedList<Animal>();
        for (Vector2d position : animalMap.keySet()) {
            if (animalMap.get(position).size() > 1) {
                List<Animal> strongest = conflict(animalMap.get(position));
                Animal dad = strongest.get(0);
                Animal mom = strongest.get(1);
                if (mom.getEnergy() >= reproduceEnergy) {//we know that dad.getEnergy()>=reproduceEnergy
                    Animal child = dad.reproduce(mom,mutation);
                    toPlace.add(child);
                    animals.add(child);
                    dad.incrementChildrenCount();
                    mom.incrementChildrenCount();
                }
            }
        }
        for (Animal animal : toPlace){
            earth.placeAnimal(animal);
        }
    }

    protected List<Animal> conflict(HashSet<Animal> animals){//to do in one line
        List<Animal> strongest = animals.stream()
                .sorted(Comparator.comparingInt(Animal::getEnergy)
                        .thenComparingInt(Animal::getLifeLength)
                        .thenComparingInt(Animal::getChildrenCount))
                .collect(Collectors.toList());
        Collections.reverse(strongest);
        return strongest;
    }
    //to do- random animals if there is no strongest
}
