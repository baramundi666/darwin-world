package agh.oop.simulation.statictics;

import agh.oop.model.objects.Animal;

import java.util.List;
import java.util.Optional;

public class AnimalStatistics {
    private final StatisticsData statisticsData;

    public AnimalStatistics(Animal animal, PlantEatenCountStatistics plantEatenCountStatistics,
                            DescendantsStatistics descendantsStatistics){
        statisticsData = new StatisticsData(animal.getGenome().getGeneList(), animal.getGenome().getActiveGene(),
                animal.getEnergy(), plantEatenCountStatistics.getPlantEatenCount(animal), animal.getChildrenCount(),
                descendantsStatistics.getNumberOfDescendants(animal), animal.getLifeLength(),
                animal.getDayOfDeath().map(Object::toString).orElse("Alive"));
    }

    public StatisticsData getStatisticsData(){
        return statisticsData;
    }
}
