package agh.oop.simulation;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.inheritance.Genome;

import java.util.*;

public class SimulationInitializer extends AbstractSimulationPart{

    private final int genomeLength;
    private final int animalNumber;
    private final int initialEnergy;

    public SimulationInitializer(Earth earth, HashSet<Animal> animals, int newPlantNumber, int plantEnergy,
                                 int genomeLength, int animalNumber, int initialEnergy, int reproduceEnergy, int[] equatorBorders) {
        super(earth, animals, new HashSet<>(), newPlantNumber, plantEnergy, reproduceEnergy, equatorBorders);
        this.genomeLength = genomeLength;
        this.animalNumber = animalNumber;
        this.initialEnergy = initialEnergy;
    }

    public HashSet<Vector2d> getNotGrownFields() {
        return new HashSet<>(notGrownFields);
    }

    public void initialize() {
        generateAnimals();
        for(int i=0; i<=earth.getBounds().upperRight().getX(); i++){
            for(int j=0; j<=earth.getBounds().upperRight().getY(); j++){
                notGrownFields.add(new Vector2d(i,j));
            }
        }
        spawnPlants();
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
}
