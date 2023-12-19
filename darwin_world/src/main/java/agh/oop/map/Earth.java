package agh.oop.map;

import agh.oop.objects.Animal;
import agh.oop.objects.Plant;
import agh.oop.objects.WorldElement;

import java.util.*;

public class Earth implements MoveValidator {
    final Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    final Map<Vector2d, Plant> plants = new HashMap<>();
    final Boundary bounds;
    public Earth(int width, int height) {
        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(width, height));
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
    public void move(Animal animal){//change it, naive approach
        Vector2d oldPosition = animal.getPosition();
        remove(animal);
        animal.move(this);
        Vector2d newPosition = animal.getPosition();
        place(animal);
        if(!newPosition.equals(oldPosition)){
            this.remove(animal);
            this.place(animal);
        }
    }
    public Optional<LinkedList<Animal>> animalsAt(Vector2d position) {
        if (animals.containsKey(position)) {
            return Optional.ofNullable(animals.get(position));
        } else {
            return Optional.empty();
        }
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
