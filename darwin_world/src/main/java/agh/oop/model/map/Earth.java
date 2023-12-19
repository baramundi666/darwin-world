package agh.oop.model.map;

import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;
import agh.oop.model.objects.WorldElement;
import agh.oop.presenter.MapChangeListener;

import java.util.*;

public class Earth implements MoveOptions {

    private final Map<Vector2d, HashSet<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    private final Boundary bounds;

    private final Set<MapChangeListener> observers = new HashSet<>();


    public Earth(int width, int height) {
        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(width, height));
    }

    public Map<Vector2d, HashSet<Animal>> getAnimals() {
        return new HashMap<>(animals);
    }

    public Map<Vector2d, Plant> getPlants() {
        return new HashMap<>(plants);
    }

    public Boundary getBounds() {
        return new Boundary(bounds.lowerLeft(), bounds.upperRight());
    }

    public void registerObserver(MapChangeListener observer){
        observers.add(observer);
    }

    public void deregisterObserver(MapChangeListener observer) {
        observers.remove(observer);
    }
    
    public void placeAnimal (Animal animal) {
        Vector2d position = animal.getPosition();
        if (!animals.containsKey(position)) {
            animals.put(position, new HashSet<>());
        }
        animals.get(position).add(animal);
    }

    public void placePlant (Plant plant) {
        Vector2d position = plant.getPosition();
        plants.put(position, plant);
    }

    public void removeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        animals.get(position).remove(animal);
    }

    public void removePlant(Plant plant) {
        Vector2d position = plant.getPosition();
        plants.remove(position);
    }

    public void move(Animal animal){
        removeAnimal(animal);
        animal.move(this);//normal move or mirror move or change direction
        placeAnimal(animal);
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
