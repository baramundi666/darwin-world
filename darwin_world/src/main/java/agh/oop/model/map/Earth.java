package agh.oop.model.map;

import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;
import agh.oop.model.objects.WorldElement;
import java.util.*;

public class Earth implements MoveOptions {
    private final Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    private final Boundary bounds;
    public Earth(int width, int height) {
        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(width, height));
    }
    public Map<Vector2d, LinkedList<Animal>> getAnimals() {
        return new HashMap<>(animals);
    }
    public Map<Vector2d, Plant> getPlants() {
        return new HashMap<>(plants);
    }
    public void place (WorldElement element) {
        Vector2d position = element.getPosition();
        if (element instanceof Animal) {
            if (!animals.containsKey(position)) {
                animals.put(position, new LinkedList<>());
            }
            animals.get(position).add((Animal) element);
        } else if (element instanceof Plant) {
            plants.put(position, (Plant) element);
        }
    }
    public void remove(WorldElement element) {
        Vector2d position = element.getPosition();
        if (element instanceof Animal) {
            for(Animal animal: animals.get(position)){
                if(animal.equals(element)){
                    animals.get(position).remove(animal);
                    if(animals.get(position).isEmpty()) animals.remove(position);
                    break;
                }
            }
        } else if (element instanceof Plant) {
            plants.remove(position);
        }
    }
    public void move(Animal animal){
        remove(animal);
        animal.move(this);//normal move or mirror move or change direction
        place(animal);
    }
    @Override
    public Optional<Vector2d> mover(Vector2d newPosition) {
        int x = newPosition.getX();
        int y = newPosition.getY();

        if(y>bounds.upperRight().getY() || y<0) return Optional.empty();
        if(x>bounds.upperRight().getX()) return Optional.of(new Vector2d(0,y));
        if(x<0) return Optional.of(new Vector2d(bounds.upperRight().getX(),y));
        return Optional.of(newPosition);
    }
}
//divide into map,mapcontroller?
