package agh.oop.simulation.statictics;

import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;

import java.util.*;

public class DescendantsStatistics implements AnimalChangeListener {
    private HashMap<Animal, HashSet<Animal>> descendants;

    public DescendantsStatistics(HashSet<Animal> animals){
        descendants = new HashMap<>();
        for (Animal animal : animals) {
            descendants.put(animal, new HashSet<>());
        }
    }

    @Override
    public void animalStateChanged(Animal child, Optional<List<Animal>> parents, Optional<Plant> plant) {
        if(parents.isPresent()){
            for(Animal parent: parents.get()){
                descendants.get(parent).add(child);
            }
            descendants.put(child, new HashSet<>());
        }
    }

    public int getNumberOfDescendants(Animal animal){
        HashSet<Animal> animalDescendants = new HashSet<>(descendants.get(animal));
        LinkedList<Animal> queue = new LinkedList<>(descendants.get(animal));
        while(!queue.isEmpty()){
            Animal current = queue.poll();
            for(Animal child: descendants.get(current)){
                if(!animalDescendants.contains(child)){
                    animalDescendants.add(child);
                    queue.add(child);
                }
            }
        }
        return animalDescendants.size();
    }

}
