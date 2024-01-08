package agh.oop.simulation.day;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.inheritance.Mutation;
import agh.oop.simulation.DataHolder;
import agh.oop.simulation.day.AbstractSimulationDay;
import agh.oop.simulation.spawner.AbstractSpawner;

import java.util.HashSet;
import java.util.LinkedList;

public class DefaultSimulationDay extends AbstractSimulationDay {

    public DefaultSimulationDay(Earth earth, HashSet<Animal> animals,
                                HashSet<Vector2d> notGrownFields,
                                AbstractSpawner spawner, Mutation mutation, DataHolder simulationParameters) {
        super(earth, animals, notGrownFields,
                spawner, mutation, simulationParameters);
    }

    @Override
    protected void moveAnimals(){
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
