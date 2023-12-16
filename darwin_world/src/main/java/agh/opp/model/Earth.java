package agh.opp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Earth implements MoveValidator{
    final Map<Vector2d, Animal> animals = new HashMap<>();
    final Map<Vector2d, Plant> plants = new HashMap<>();
    final Boundary bounds;

    public Earth(int width, int height) {
        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(width, height));
    }

    public void place (WorldElement element) {
        Vector2d position = element.getPosition();
        if (element instanceof Animal) {
            animals.put(position, (Animal) element);
        } else if (element instanceof Plant) {
            plants.put(position, (Plant) element);
        }
    }

    public void remove(WorldElement element) {
        Vector2d position = element.getPosition();
        if (element instanceof Animal) {
            animals.remove(position);
        } else if (element instanceof Plant) {
            plants.remove(position);
        }
    }

    public void move(Animal animal){
        Vector2d oldPosition = animal.getPosition();
        animal.move(this);
        Vector2d newPosition = animal.getPosition();

        if(!newPosition.equals(oldPosition)){
            animals.remove(oldPosition);
            animals.put(newPosition,animal);
        }
        if(plants.containsKey(newPosition)){
            Plant plant = plants.get(newPosition);
            animal.eat(plant);
            remove(plant);
        }
    }//to implement, depends on solution of animal behaviour

    public Optional<WorldElement> objectAt(Vector2d position) {
        if (animals.containsKey(position)) {
            return Optional.ofNullable(animals.get(position));
        } else if (plants.containsKey(position)) {
            return Optional.ofNullable(plants.get(position));
        } else {
            return Optional.empty();
        }
    }//return optional of world element at given position or empty optional

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
