package agh.oop.simulation;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.inheritance.Genome;
import agh.oop.simulation.data.SimulationData;
import agh.oop.simulation.spawner.AbstractSpawner;

import java.util.*;

public class SimulationInitializer{

    private final int genomeLength;
    private final int animalNumber;
    private final int initialEnergy;

    private final Earth earth;
    private final HashSet<Animal> animals;
    private final int copulateEnergy;
    private final AbstractSpawner spawner;

    public SimulationInitializer(Earth earth, HashSet<Animal> animals,
                                 AbstractSpawner spawner, SimulationData simulationParameters) {
        this.genomeLength = simulationParameters.genomeLength();
        this.animalNumber = simulationParameters.newAnimalNumber();
        this.initialEnergy = simulationParameters.initialEnergy();
        this.earth = earth;
        this.animals = animals;
        this.copulateEnergy = simulationParameters.copulateEnergy();
        this.spawner = spawner;
    }

    public void initialize() {
        generateAnimals();
        spawner.spawnPlants();
    }

    private Genome generateGenome(){
        List<Integer> geneList = new ArrayList<>();
        for(int i=0; i<genomeLength; i++){
            geneList.add((int)(Math.random()*8));
        }
        return new Genome(geneList, genomeLength);
    }

    private void generateAnimals(){
        List<Vector2d> freePositions = new ArrayList<>();
        for(int i=0; i<=earth.getBounds().upperRight().getX(); i++){
            for(int j=0; j<=earth.getBounds().upperRight().getY(); j++){
                freePositions.add(new Vector2d(i,j));
            }
        }
        Collections.shuffle(freePositions);
        for(int i=0; i<animalNumber; i++){
            var animal = new Animal(freePositions.get(i), initialEnergy, generateGenome(), copulateEnergy);
            animals.add(animal);
            earth.placeAnimal(animal);
        }
    }
}
