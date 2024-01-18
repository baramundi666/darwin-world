package agh.oop.model.map;

import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;
import agh.oop.model.objects.inheritance.Genome;
import javafx.geometry.Bounds;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class EarthTest {

    @Test
    void placeAnimalInBounds() {
        Earth earth = new Earth(10, 10);
        List<Integer> geneList = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        Genome genome = new Genome(geneList, geneList.size());
        Animal animal = new Animal(new Vector2d(5, 5),10,genome,10);
        earth.placeAnimal(animal);
        assertEquals(animal, earth.getAnimals().get(new Vector2d(5, 5)).iterator().next());
    }

    @Test
    void placeAnimalOutOfBounds() {
        Earth earth = new Earth(10, 10);
        List<Integer> geneList = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        Genome genome = new Genome(geneList, geneList.size());
        Animal animal = new Animal(new Vector2d(10, 10),10,genome,10);
        assertThrows(IllegalArgumentException.class, () -> earth.placeAnimal(animal));
    }

    @Test
    void placePlantInBounds() {
        Earth earth = new Earth(10, 10);
        Plant plant = new Plant(new Vector2d(5, 5),3,false);
        earth.placePlant(plant);
        assertEquals(plant, earth.getPlants().get(new Vector2d(5, 5)));
    }

    @Test
    void placePlantOutOfBounds() {
        Earth earth = new Earth(10, 10);
        Plant plant = new Plant(new Vector2d(11, 11),3,false);
        assertThrows(IllegalArgumentException.class, () -> earth.placePlant(plant));
    }

    @Test
    void removeAnimal() {
        Earth earth = new Earth(10, 10);
        List<Integer> geneList = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        Genome genome = new Genome(geneList, geneList.size());
        Animal animal = new Animal(new Vector2d(5, 5),10,genome,10);
        earth.placeAnimal(animal);
        earth.removeAnimal(animal, Optional.empty());
        assertTrue(earth.getAnimals().isEmpty());
    }

    @Test
    void removePlant() {
        Earth earth = new Earth(10, 10);
        Plant plant = new Plant(new Vector2d(5, 5),3,false);
        earth.placePlant(plant);
        earth.removePlant(plant);
        assertTrue(earth.getPlants().isEmpty());
    }

    @Test
    void moveAnimal() {
        Earth earth = new Earth(10, 10);
        List<Integer> geneList = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        Genome genome = new Genome(geneList, geneList.size());
        Animal animal = new Animal(new Vector2d(5, 5),10,genome,10);
        earth.placeAnimal(animal);

        earth.move(animal);

        assertFalse(earth.getAnimals().containsKey(new Vector2d(5, 5)));
        assertTrue(earth.getAnimals().containsKey(animal.getPosition()));
    }

    @Test
    void moverInBounds() {
        Earth earth = new Earth(10, 10);
        Optional<Vector2d> newPosition = earth.mover(new Vector2d(5, 5));
        assertTrue(newPosition.isPresent());
        assertEquals(new Vector2d(5, 5), newPosition.get());
    }

    @Test
    void moverOutOfBounds() {
        Earth earth = new Earth(10, 10);
        Optional<Vector2d> newPosition = earth.mover(new Vector2d(11, 11));
        assertTrue(newPosition.isEmpty());
    }

    @Test
    void isInBounds() {
        Earth earth = new Earth(10, 10);
        assertTrue(earth.isInBounds(new Vector2d(5, 5)));
        assertFalse(earth.isInBounds(new Vector2d(11, 11)));
    }
}