package agh.oop.simulation.statictics;

import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;

import java.util.List;
import java.util.Optional;

public interface AnimalChangeListener {
    void animalStateChanged(Animal animal, Optional<List<Animal>> parents, Optional<Plant> plant);
}
