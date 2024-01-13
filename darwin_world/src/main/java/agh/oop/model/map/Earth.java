package agh.oop.model.map;

import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;
import java.util.UUID;

import java.util.*;

public class Earth implements MapOptions {

    private final Map<Vector2d, HashSet<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    private final Boundary bounds;
    private final HashSet<Animal> allAnimals = new HashSet<>();
    private final UUID id  = UUID.randomUUID();


    public Earth(int width, int height) {
        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(width-1, height-1));
    }

    public synchronized Map<Vector2d, HashSet<Animal>> getAnimals() {
        return new HashMap<>(animals);
    }

    public synchronized Map<Vector2d, Plant> getPlants() {
        return new HashMap<>(plants);
    }

    public Boundary getBounds() {
        return new Boundary(bounds.lowerLeft(), bounds.upperRight());
    }
    public UUID getId() {
        return id;
    }
    public int getArea(){
        return (bounds.upperRight().getX()+1)*(bounds.upperRight().getY()+1);
    }

    public HashSet<Animal> getDeadAnimals() {
        HashSet<Animal> deadAnimals = new HashSet<>();
        for (Animal animal : allAnimals) {
            if (animal.isDead()) {
                deadAnimals.add(animal);
            }
        }
        return deadAnimals;
    }
    public HashSet<Animal> getAliveAnimals() {
        HashSet<Animal> aliveAnimals = new HashSet<>();
        for (Animal animal : allAnimals) {
            if (!animal.isDead()) {
                aliveAnimals.add(animal);
            }
        }
        return aliveAnimals;
    }

    public void placeAnimal (Animal animal) {
        Vector2d position = animal.getPosition();
//        if (!isInBounds(position)) throw new IllegalArgumentException;
        if (!animals.containsKey(position)) {
            animals.put(position, new HashSet<>());
        }
        animals.get(position).add(animal);
        allAnimals.add(animal);
    }

    public void placePlant (Plant plant) {
        Vector2d position = plant.getPosition();
//        if (!isInBounds(position)) throw new IllegalArgumentException;
        plants.put(position, plant);
    }

    public void removeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        animals.get(position).remove(animal);
        if (animals.get(position).isEmpty()) {
            animals.remove(position);
        }
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

    @Override
    public boolean isInBounds(Vector2d position) {
        int x = position.getX();
        int y = position.getY();
        return x>=bounds.lowerLeft().getX() && y>=bounds.lowerLeft().getY() &&
                x<=bounds.upperRight().getX() && y<=bounds.upperRight().getX();
    }
}
