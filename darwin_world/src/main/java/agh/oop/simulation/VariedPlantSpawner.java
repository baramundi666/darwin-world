package agh.oop.simulation;

import agh.oop.model.map.Boundary;
import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Plant;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class VariedPlantSpawner extends AbstractSpawner{
    private Boundary poisonousAreaBorders;
    public VariedPlantSpawner(Earth earth, int newPlantNumber, int plantEnergy) {
        super(earth, newPlantNumber, plantEnergy);
    }

    @Override
    public void spawnPlants(){}
}
