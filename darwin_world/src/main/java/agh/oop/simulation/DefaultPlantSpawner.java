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

public class DefaultPlantSpawner extends AbstractSpawner{
    private final Boundary equatorBorders;

    public DefaultPlantSpawner(Earth earth, int newPlantNumber, int plantEnergy) {
        super(earth, newPlantNumber, plantEnergy);
        int lowerEquatorBorder = (int)(Math.ceil(earth.getBounds().upperRight().getY()/5.0 *2));
        int upperEquatorBorder = lowerEquatorBorder + (int)(Math.ceil((earth.getBounds().upperRight().getY()+1)/5.0)-1);
        this.equatorBorders = new Boundary(new Vector2d(0,lowerEquatorBorder), new Vector2d(earth.getBounds().upperRight().getX(),upperEquatorBorder));
    }

    public Boundary getEquatorBorders(){
        return equatorBorders;
    }

    @Override
    public void spawnPlants(){
        var lowerBorder = equatorBorders.lowerLeft().getY();
        var upperBorder = equatorBorders.upperRight().getY();
        List<Vector2d> notGrownEquatorList = notGrownFields.stream()
                .filter(position -> position.getY() >= lowerBorder && position.getY() <= upperBorder)
                .collect(Collectors.toList());
        List<Vector2d> notGrownSteppeList = notGrownFields.stream()
                .filter(position -> position.getY() < lowerBorder || position.getY() > upperBorder)
                .collect(Collectors.toList());
        Collections.shuffle(notGrownEquatorList);
        Collections.shuffle(notGrownSteppeList);

        Iterator<Vector2d> equatorIterator = notGrownEquatorList.iterator();
        Iterator<Vector2d> steppeIterator = notGrownSteppeList.iterator();

        for(int i=0; i<newPlantNumber && i<notGrownFields.size(); i++){
            Vector2d position;
            int random = (int)(Math.random()*5);
            if((random<4 || !steppeIterator.hasNext()) && equatorIterator.hasNext() ){
                position = equatorIterator.next();
            }
            else {
                position = steppeIterator.next();
            }
            Plant newPlant = new Plant(position ,plantEnergy,false);
            notGrownFields.remove(position);
            earth.placePlant(newPlant);
        }
    }

}
