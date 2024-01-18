package agh.oop.simulation.statictics;

import java.util.List;

public record StatisticsData(List<Integer> genotype, int activeGene, int energy, int plantEatenCount,
                                         int childrenCount, int descendantsCount, int lifeLength, String dayOfDeath) {}
