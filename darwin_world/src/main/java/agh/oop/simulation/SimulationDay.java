package agh.oop.simulation;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;

import java.util.*;
import java.util.stream.Collectors;

public class SimulationDay {
    private final Earth earth;
    private final HashSet<Animal> animals;
    private final HashSet<Vector2d> notGrownFields;
    private final int reproduceEnergy;
    private final int newPlantNumber;
    private final int plantEnergy;

    public SimulationDay(Earth earth, HashSet<Animal> animals, HashSet<Vector2d> notGrownFields, int newPlantNumber, int plantEnergy, int reproduceEnergy){
        this.earth = earth;
        this.animals = animals;
        this.notGrownFields = notGrownFields;
        this.reproduceEnergy = reproduceEnergy;
        this.newPlantNumber = newPlantNumber;
        this.plantEnergy = plantEnergy;
    }

    public void run(){
        for(Animal animal: animals){
            if(animal.getEnergy()<=0){//remove dead
                earth.removeAnimal(animal);
                animals.remove(animal);
            }
            else{
                earth.move(animal);//move alive
            }
        }
        animalsEat();
        reproduceAnimals();
        spawnPlants();
    }

    void animalsEat(){
        for(Vector2d position: earth.getAnimals().keySet()){
            if(earth.getPlants().containsKey(position)){
                List<Animal> strongest = conflict(earth.getAnimals().get(position));
                strongest.get(0).eat(earth.getPlants().get(position));
                earth.removePlant(earth.getPlants().get(position));
                notGrownFields.add(position);
            }
        }
    }

    void reproduceAnimals(){
        for(Vector2d position: earth.getAnimals().keySet()) {//reproduce
            if (earth.getAnimals().get(position).size() > 1) {
                List<Animal> strongest = conflict(earth.getAnimals().get(position));
                if(strongest.get(1).getEnergy()>reproduceEnergy) {
                    Animal child = strongest.get(0).reproduce(strongest.get(1));
                    earth.placeAnimal(child);
                    animals.add(child);
                    strongest.get(0).incrementChildrenCount();
                    strongest.get(1).incrementChildrenCount();
                }
            }
        }
    }
    void spawnPlants(){
        List<Vector2d> notGrownFieldsList = new ArrayList<>(notGrownFields);
        Collections.shuffle(notGrownFieldsList);
        for(int i=0; i<newPlantNumber && i<notGrownFieldsList.size(); i++){//grow
            Plant newPlant = new Plant(notGrownFieldsList.get(i),plantEnergy,false);
            notGrownFields.remove(notGrownFieldsList.get(i));
            earth.placePlant(newPlant);
        }
    }

    private List<Animal> conflict(HashSet<Animal> animals){
        return animals.stream()
                .sorted(Comparator
                        .comparingInt(Animal::getEnergy)
                        .thenComparingInt(Animal::getLifeLength)
                        .thenComparingInt(Animal::getChildrenCount))
                .limit(2)
                .collect(Collectors.toList());
    }

}
