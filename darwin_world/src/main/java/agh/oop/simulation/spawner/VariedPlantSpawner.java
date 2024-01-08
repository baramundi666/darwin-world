package agh.oop.simulation.spawner;

import agh.oop.model.map.Boundary;
import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Plant;
import agh.oop.simulation.DataHolder;
import agh.oop.simulation.spawner.AbstractSpawner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VariedPlantSpawner extends AbstractSpawner {
    private final Boundary poisonousAreaBorders;
    public VariedPlantSpawner(Earth earth, DataHolder simulationParameters) {
        super(earth, simulationParameters);
        this.poisonousAreaBorders = generatePoisonousAreaBorders();
    }

    private Boundary generatePoisonousAreaBorders(){
        int width = (int) earth.getBounds().upperRight().getX() + 1;
        int height = (int) earth.getBounds().upperRight().getY() + 1;
        int size = (int) Math.ceil(Math.sqrt(width * height * 0.2));

        int x = (int) Math.floor(Math.random() * (width - size));
        int y = (int) Math.floor(Math.random() * (height - size));

        return new Boundary(new Vector2d(x, y), new Vector2d(x + size, y + size));
    }

    public boolean isPoisonousArea(Vector2d position){
        return position.getX() >= poisonousAreaBorders.lowerLeft().getX() &&
                position.getX() <= poisonousAreaBorders.upperRight().getX() &&
                position.getY() >= poisonousAreaBorders.lowerLeft().getY() &&
                position.getY() <= poisonousAreaBorders.upperRight().getY();
    }

    @Override
    public void spawnPlants(){
        List<Vector2d> notGrownFieldsList = new ArrayList<>(notGrownFields);
        Collections.shuffle(notGrownFieldsList);

        for(int i = 0;i<newPlantNumber && i<notGrownFields.size();i++){
            Vector2d position = notGrownFieldsList.get(i);
            if(isPoisonousArea(position)){
                int random = (int)(Math.random()*2);
                if (random == 0){
                    Plant newPlant = new Plant(position ,plantEnergy,true);
                    earth.placePlant(newPlant);
                }
            }
            else{
                Plant newPlant = new Plant(position ,plantEnergy,false);
                earth.placePlant(newPlant);
            }
            notGrownFields.remove(position);
        }
    }

    @Override
    public Boundary getSpecialAreaBorders(){
        return poisonousAreaBorders;
    }
}
