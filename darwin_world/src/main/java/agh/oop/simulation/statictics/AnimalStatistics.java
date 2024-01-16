package agh.oop.simulation.statictics;

import agh.oop.model.objects.Animal;

import java.util.List;
import java.util.Optional;

public class AnimalStatistics {
    private List<Integer> genotype;
    private int activeGene;
    private int energy;
    private int plantEatenCount;
    private int childrenCount;
    private int descendantsCount;
    private int lifeLength;
    private Optional<Integer> dayOfDeath = Optional.empty();

    public void setAnimalStatistics(Animal animal, PlantEatenCountStatistics plantEatenCountStatistics,
                                    DescendantsStatistics descendantsStatistics){
        genotype = animal.getGenome().getGeneList();
        activeGene = animal.getGenome().getActiveGene();
        energy = animal.getEnergy();
        plantEatenCount = plantEatenCountStatistics.getPlantEatenCount(animal);
        childrenCount = animal.getChildrenCount();
        descendantsCount = descendantsStatistics.getNumberOfDescendants(animal);
        lifeLength = animal.getLifeLength();
        dayOfDeath = animal.getDayOfDeath();
    }

    public int getActiveGene() {
        return activeGene;
    }

    public int getEnergy() {
        return energy;
    }

    public int getPlantEatenCount() {
        return plantEatenCount;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public int getDescendantsCount() {
        return descendantsCount;
    }

    public int getLifeLength() {
        return lifeLength;
    }

    public String getDayOfDeath() {
        return dayOfDeath.map(Object::toString).orElse("Alive");
    }
}
