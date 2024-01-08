package agh.oop.simulation.spawner;

import agh.oop.model.map.Boundary;
import agh.oop.model.map.Earth;
import agh.oop.simulation.spawner.AbstractSpawner;

public class VariedPlantSpawner extends AbstractSpawner {
    private Boundary poisonousAreaBorders;
    public VariedPlantSpawner(Earth earth, int newPlantNumber, int plantEnergy) {
        super(earth, newPlantNumber, plantEnergy);
    }

    @Override
    public void spawnPlants(){}
}
