package agh.oop.simulation;

public record DataHolder(int simulationLength, int reproduceEnergy, int copulateEnergy, int newPlantNumber,
                         int plantEnergy, int newAnimalNumber, int genomeLength, int initialEnergy,
                         int[] mutationRange, String mutationVariant, String mapVariant) {}
