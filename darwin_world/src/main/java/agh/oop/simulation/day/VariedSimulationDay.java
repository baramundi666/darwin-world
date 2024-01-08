package agh.oop.simulation.day;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.inheritance.Mutation;
import agh.oop.simulation.spawner.AbstractSpawner;

import java.util.HashSet;
import java.util.LinkedList;

public class VariedSimulationDay extends AbstractSimulationDay {
    public VariedSimulationDay(Earth earth, HashSet<Animal> animals,
                                HashSet<Vector2d> notGrownFields, int newPlantNumber,
                                int plantEnergy, int reproduceEnergy,
                                AbstractSpawner spawner, Mutation mutation) {
        super(earth, animals, notGrownFields,
                newPlantNumber, plantEnergy, reproduceEnergy, spawner, mutation);
    }

    @Override
    protected void moveAnimals() {//to do- animals can notice poisonous plants
        var animalMap = earth.getAnimals();
        var toMove = new LinkedList<Animal>();
        for (Vector2d position : animalMap.keySet()) {
            toMove.addAll(animalMap.get(position));
        }
        for (Animal animal : toMove){
            earth.move(animal);
        }
    }
}
