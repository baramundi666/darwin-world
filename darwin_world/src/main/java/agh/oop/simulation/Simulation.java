package agh.oop.simulation;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.inheritance.Genome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Simulation {
    private final Earth earth;
    private final int reproduceEnergy;
    private final int newPlantNumber;
    private final int plantEnergy;
    private final int animalNumber;
    private final int genomeLength;
    private final int initialEnergy;
    private final HashSet<Animal> animals;

    public Simulation(Earth earth, int reproduceEnergy, int newPlantNumber, int plantEnergy, int animalNumber, int genomeLength, int initialEnergy){
        //to do better implementation of free fields list
        this.earth = earth;
        this.reproduceEnergy = reproduceEnergy;
        this.newPlantNumber = newPlantNumber;
        this.plantEnergy = plantEnergy;
        this.animalNumber = animalNumber;
        this.genomeLength = genomeLength;
        this.initialEnergy = initialEnergy;
        this.animals = new HashSet<>();
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
            var animal = new Animal(freePositions.get(i), initialEnergy, generateGenome(), reproduceEnergy);
            animals.add(animal);
            earth.placeAnimal(animal);
        }
    }

    public void start(){
        generateAnimals();
        HashSet<Vector2d> notGrownFields = new HashSet<>();
        for(int i=0; i<=earth.getBounds().upperRight().getX(); i++){
            for(int j=0; j<=earth.getBounds().upperRight().getY(); j++){
                notGrownFields.add(new Vector2d(i,j));
            }
        }
        SimulationDay simulationDay = new SimulationDay(earth,animals,notGrownFields, newPlantNumber,plantEnergy, reproduceEnergy);
        simulationDay.spawnPlants();
        for(int i=0;i<10;i++){
            simulationDay.run();
        }
    }
}
