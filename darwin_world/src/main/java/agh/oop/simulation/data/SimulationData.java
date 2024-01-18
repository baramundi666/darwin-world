package agh.oop.simulation.data;

public record SimulationData(int simulationLength, int reproduceEnergy, int copulateEnergy, int newPlantNumber,
                             int plantEnergy, int newAnimalNumber, int genomeLength, int initialEnergy,
                             int[] mutationRange, String mutationVariant, String mapVariant) {}
