package agh.oop.simulation;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;

import java.util.*;
import java.util.stream.Collectors;

public class SimulationDay{
    private final Earth earth;
    private final HashSet<Animal> animals;//statistics purpose
    private final HashSet<Vector2d> notGrownFields;
    private final int reproduceEnergy;
    private final int newPlantNumber;
    private final int plantEnergy;
    public int notGrownEquatorFields;
    public final int[] equatorBorders;

    public SimulationDay(Earth earth, HashSet<Animal> animals, HashSet<Vector2d> notGrownFields, int newPlantNumber, int plantEnergy, int reproduceEnergy){
        this.earth = earth;
        this.animals = animals;
        this.notGrownFields = notGrownFields;
        this.reproduceEnergy = reproduceEnergy;
        this.newPlantNumber = newPlantNumber;
        this.plantEnergy = plantEnergy;
        int lowerEquatorBorder = (int)(Math.ceil(earth.getBounds().upperRight().getY()/5.0 * 2));
        int upperEquatorBorder = lowerEquatorBorder + (int)(Math.ceil((earth.getBounds().upperRight().getY()+1)/5.0)-1);
        this.equatorBorders = new int[]{lowerEquatorBorder, upperEquatorBorder};
        this.notGrownEquatorFields = (equatorBorders[1]-equatorBorders[0]+1) * (earth.getBounds().upperRight().getX()+1) - this.newPlantNumber;
    }

    public void run(){
            removeDeadAnimals();
            moveAnimals();
            animalsEat();
            reproduceAnimals();
            spawnPlants();
    }

    private void removeDeadAnimals(){
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

    private void moveAnimals(){
        var animalMap = earth.getAnimals();
        var toMove = new LinkedList<Animal>();
        if(!animalMap.isEmpty()) {
            for (Vector2d position : animalMap.keySet()) {
                if (!animalMap.get(position).isEmpty()) {
                    toMove.addAll(animalMap.get(position));
                }
            }
        }
        for (Animal animal : toMove){
            earth.move(animal);
        }
    }

    private void animalsEat(){
        var animalMap = earth.getAnimals();
        var plantMap = earth.getPlants();
        var toBeEaten = new HashMap<Vector2d, Animal>();
        if(!animalMap.isEmpty() && !plantMap.isEmpty()) {
            for (Vector2d position : animalMap.keySet()) {
                if (plantMap.containsKey(position)) {
                    if (!animalMap.get(position).isEmpty()) {
                        List<Animal> strongest = conflict(animalMap.get(position));
                        toBeEaten.put(position, strongest.get(0));
                        notGrownFields.add(position);
                    }
                }
            }
        }
        for (Vector2d position : toBeEaten.keySet()) {
            toBeEaten.get(position).eat(plantMap.get(position));
            earth.removePlant(plantMap.get(position));
            if(position.getY()>=equatorBorders[0] && position.getY()<=equatorBorders[1]){
                notGrownEquatorFields++;
            }
        }
    }

    private void reproduceAnimals(){
        var animalMap = earth.getAnimals();
        var toPlace = new LinkedList<Animal>();
        if(!animalMap.isEmpty()) {
            for (Vector2d position : animalMap.keySet()) {
                if (animalMap.get(position).size() > 1) {
                    List<Animal> strongest = conflict(animalMap.get(position));
                    Animal dad = strongest.get(0);
                    Animal mom = strongest.get(1);
                    if (mom.getEnergy() >= reproduceEnergy) {//we know that dad.getEnergy()>=reproduceEnergy
                        Animal child = dad.reproduce(mom);
                        toPlace.add(child);
                        animals.add(child);
                        dad.incrementChildrenCount();
                        mom.incrementChildrenCount();
                    }
                }
            }
        }
        for (Animal animal : toPlace){
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
