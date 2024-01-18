package agh.oop.simulation.day;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.inheritance.Mutation;
import agh.oop.simulation.data.SimulationData;
import agh.oop.simulation.spawner.AbstractSpawner;

import java.util.HashSet;
import java.util.LinkedList;

public class VariedSimulationDay extends AbstractSimulationDay {
    public VariedSimulationDay(Earth earth, HashSet<Animal> animals,
                               HashSet<Vector2d> notGrownFields,
                               AbstractSpawner spawner, Mutation mutation, SimulationData simulationParameters) {
        super(earth, animals, notGrownFields,
                spawner, mutation, simulationParameters);
    }

    @Override
    protected void moveAnimals() {
        var animalMap = earth.getAnimals();
        var toMove = new LinkedList<Animal>();
        for (Vector2d position : animalMap.keySet()) {
            toMove.addAll(animalMap.get(position));
        }
        var plantsMap = earth.getPlants();
        for (Animal animal : toMove){
            var position = animal.getPosition();
            var direction = animal.getDirection().shift(animal.getGenome().getActiveGeneValue());
            var newPosition = position.add(direction.toVector());

            boolean hasBeenAlreadyMoved = false;
            if (plantsMap.containsKey(newPosition)) {
                if (plantsMap.get(newPosition).isPoisonous()) {
                    var random = (int) (Math.random()*5);
                    if (random==0) {
                        var randomShift = (int) (Math.random()*7);
                        animal.setDirection(direction.shift(randomShift));
                        earth.move(animal);
                        animal.setDirection(direction);
                        hasBeenAlreadyMoved = true;
                    }
                }
            }
            if (!hasBeenAlreadyMoved) {
                earth.move(animal);
            }
        }
    }
}
