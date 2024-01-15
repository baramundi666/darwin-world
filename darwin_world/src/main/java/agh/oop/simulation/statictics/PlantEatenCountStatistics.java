package agh.oop.simulation.statictics;

import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class PlantEatenCountStatistics implements AnimalChangeListener {
    private final HashMap<Animal,Integer> plantEatenCount;

    public PlantEatenCountStatistics(HashSet<Animal> animals){
        plantEatenCount = new HashMap<>();
        for (Animal animal : animals) {
            plantEatenCount.put(animal, 0);
        }
    }


    @Override
    public void animalStateChanged(Animal animal, Optional<List<Animal>> parents, Optional<Plant> plant) {
        if(plant.isPresent()){
            plantEatenCount.put(animal, plantEatenCount.get(animal)+1);
        }
        if(parents.isPresent()){
            plantEatenCount.put(animal,0);
        }
    }

    public int getPlantEatenCount(Animal animal){
        return plantEatenCount.get(animal);
    }
}
