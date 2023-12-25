package agh.oop.simulation;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSimulationPart {

    protected final Earth earth;
    protected final HashSet<Animal> animals;//statistics purpose
    protected final HashSet<Vector2d> notGrownFields;
    protected final int reproduceEnergy;
    protected final int newPlantNumber;
    protected final int plantEnergy;
    protected int notGrownEquatorFields;
    protected final int[] equatorBorders;

    public AbstractSimulationPart(Earth earth, HashSet<Animal> animals, HashSet<Vector2d> notGrownFields,
                         int newPlantNumber, int plantEnergy, int reproduceEnergy){
        this.earth = earth;
        this.animals = animals;
        this.notGrownFields = notGrownFields;
        this.reproduceEnergy = reproduceEnergy;
        this.newPlantNumber = newPlantNumber;
        this.plantEnergy = plantEnergy;
        int lowerEquatorBorder = (int)(Math.ceil(earth.getBounds().upperRight().getY()/5.0 * 2));
        int upperEquatorBorder = lowerEquatorBorder + (int)(Math.ceil((earth.getBounds().upperRight().getY()+1)/5.0)-1);
        this.equatorBorders = new int[]{lowerEquatorBorder, upperEquatorBorder};
        this.notGrownEquatorFields = (equatorBorders[1]-equatorBorders[0]+1) * (earth.getBounds().upperRight().getX()+1) - this.newPlantNumber;
    }

    protected void spawnPlants(){
        List<Vector2d> notGrownEquatorList = notGrownFields.stream()
                .filter(position -> position.getY() >= equatorBorders[0] && position.getY() <= equatorBorders[1])
                .collect(Collectors.toList());
        List<Vector2d> notGrownSteppeList = notGrownFields.stream()
                .filter(position -> position.getY() < equatorBorders[0] || position.getY() > equatorBorders[1])
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
                notGrownEquatorFields--;
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
