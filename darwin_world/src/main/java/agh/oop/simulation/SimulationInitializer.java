package agh.oop.simulation;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;
import agh.oop.model.objects.inheritance.Genome;

import java.util.*;
import java.util.stream.Collectors;

public class SimulationInitializer {

    private final Earth earth;
    private final HashSet<Animal> animals;//statistics purpose
    private final HashSet<Vector2d> notGrownFields;
    private final int reproduceEnergy;
    private final int newPlantNumber;
    private final int plantEnergy;

    private final int animalNumber;
    private final int genomeLength;
    private final int initialEnergy;
    public int notGrownEquatorFields;
    public final int[] equatorBorders;

    public SimulationInitializer(Earth earth, HashSet<Animal> animals,
                                 int newPlantNumber, int plantEnergy, int reproduceEnergy,
                                 int animalNumber, int genomeLength, int initialEnergy){
        this.earth = earth;
        this.notGrownFields = new HashSet<>();
        this.newPlantNumber = newPlantNumber;
        this.plantEnergy = plantEnergy;
        this.reproduceEnergy = reproduceEnergy;
        this.animalNumber = animalNumber;
        this.genomeLength = genomeLength;
        this.initialEnergy = initialEnergy;
        this.animals = animals;
        int lowerEquatorBorder = (int)(Math.ceil(earth.getBounds().upperRight().getY()/5.0 * 2));
        int upperEquatorBorder = lowerEquatorBorder + (int)(Math.ceil((earth.getBounds().upperRight().getY()+1)/5.0)-1);
        this.equatorBorders = new int[]{lowerEquatorBorder, upperEquatorBorder};
        this.notGrownEquatorFields = (equatorBorders[1]-equatorBorders[0]+1) * (earth.getBounds().upperRight().getX()+1) - this.newPlantNumber;
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

    void spawnPlants(){
        List<Vector2d> notGrownEquatorList = notGrownFields.stream()
                .filter(position -> position.getY() >= equatorBorders[0] && position.getY() <= equatorBorders[1])
                .collect(Collectors.toList());
        List<Vector2d> notGrownSteppeList = notGrownFields.stream()
                .filter(position -> position.getY() < equatorBorders[0] || position.getY() > equatorBorders[1])
                .collect(Collectors.toList());
        Collections.shuffle(notGrownEquatorList);
        Collections.shuffle(notGrownSteppeList);

        Iterator<Vector2d> equatorIterator = notGrownEquatorList.iterator();
        Iterator<Vector2d> steppeIterator = notGrownSteppeList.iterator();
        for(int i=0; i<newPlantNumber && i<notGrownFields.size(); i++){
            Vector2d position;
            int random = (int)(Math.random()*5);
            if((random<4 || !steppeIterator.hasNext()) && equatorIterator.hasNext() ){
                position = equatorIterator.next();
                notGrownEquatorFields--;
            }
            else {
                position = steppeIterator.next();
            }
            Plant newPlant = new Plant(position ,plantEnergy,false);
            notGrownFields.remove(position);
            earth.placePlant(newPlant);
        }
    }
}
