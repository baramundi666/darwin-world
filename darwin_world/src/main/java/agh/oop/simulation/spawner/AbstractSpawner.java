package agh.oop.simulation.spawner;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;

import java.util.HashSet;

public abstract class AbstractSpawner {

    protected final Earth earth;
    protected final HashSet<Vector2d> notGrownFields;
    protected final int newPlantNumber;
    protected final int plantEnergy;

    public AbstractSpawner(Earth earth, int newPlantNumber, int plantEnergy){
        this.earth = earth;
        this.notGrownFields = new HashSet<>();
        for(int i=0; i<=earth.getBounds().upperRight().getX(); i++){
            for(int j=0; j<=earth.getBounds().upperRight().getY(); j++){
                notGrownFields.add(new Vector2d(i,j));
            }
        }
        this.newPlantNumber = newPlantNumber;
        this.plantEnergy = plantEnergy;
    }

    public HashSet<Vector2d> getNotGrownFields() {
        return new HashSet<>(notGrownFields);
    }

    public abstract void spawnPlants();
}
