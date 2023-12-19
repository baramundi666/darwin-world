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

    public void run(){;
        removeDeadAnimals();
        moveAnimals();
        //animalsEat();
        //reproduceAnimals();
        spawnPlants();
    }
    void removeDeadAnimals(){
        for(Animal animal: animals){
            if(animal.getEnergy()<=0){//remove dead
                earth.removeAnimal(animal);
                animals.remove(animal);
            }
        }
    }

    void moveAnimals(){
        for(Animal animal: animals){
            earth.move(animal);
        }
    }

    void animalsEat(){
        for(Vector2d position: earth.getAnimals().keySet()){
            if(earth.getPlants().containsKey(position)){
                if(earth.getAnimals().get(position).size()==1){
                    earth.getAnimals().get(position).iterator().next().eat(earth.getPlants().get(position));
                    earth.removePlant(earth.getPlants().get(position));
                    notGrownFields.add(position);
                }
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
                Animal dad = strongest.get(0);
                Animal mom = strongest.get(1);
                if(mom.getEnergy()>=reproduceEnergy) {//we know that dad.getEnergy()>=reproduceEnergy
                    Animal child = dad.reproduce(mom);
                    earth.placeAnimal(child);
                    animals.add(child);
                    dad.incrementChildrenCount();
                    mom.incrementChildrenCount();
                }
            }
        }
    }
    void spawnPlants(){
        List<Vector2d> notGrownFieldsList = new ArrayList<>(notGrownFields);
        Collections.shuffle(notGrownFieldsList);
        for(int i=0; i<newPlantNumber && i<notGrownFieldsList.size(); i++){
            //what to do when there is no space for all plants
            //to do add poison plants
            Plant newPlant = new Plant(notGrownFieldsList.get(i),plantEnergy,false);
            notGrownFields.remove(notGrownFieldsList.get(i));
            earth.placePlant(newPlant);
        }
    }

    private List<Animal> conflict(HashSet<Animal> animals){//to do in one line
        List<Animal> strongest = animals.stream()
                .sorted(Comparator
                        .comparingInt(Animal::getEnergy)
                        .thenComparingInt(Animal::getLifeLength)
                        .thenComparingInt(Animal::getChildrenCount))
                .collect(Collectors.toList());
        Collections.reverse(strongest);
        return strongest;
    }
}
