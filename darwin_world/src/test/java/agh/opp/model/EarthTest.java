package agh.opp.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EarthTest {

    @Test
    void place() {
        Earth earth = new Earth(10,10);
        Vector2d position1 = new Vector2d(1,2);
        Vector2d position2 = new Vector2d(3,4);
        List<Integer> genomeList = List.of(1,2);
        Genome genome = new Genome(genomeList, new int[]{1, 2},1);
        Animal animal = new Animal(position1,2,genome,MapDirection.N);
        Animal animal2 = new Animal(position1,2,genome,MapDirection.N);
        Animal animal3 = new Animal(position2,2,genome,MapDirection.N);

        earth.place(animal);
        earth.place(animal3);

        assertTrue(earth.animalsAt(position1).get().contains(animal));
        assertFalse(earth.animalsAt(position1).get().contains(animal2));
        assertTrue(earth.animalsAt(position2).get().contains(animal3));
    }

    @Test
    void remove() {
        Earth earth = new Earth(10,10);
        Vector2d position1 = new Vector2d(1,2);
        Vector2d position2 = new Vector2d(3,4);
        List<Integer> genomeList = List.of(1,2);
        Genome genome = new Genome(genomeList, new int[]{1, 2},1);
        Animal animal = new Animal(position1,2,genome,MapDirection.N);
        Plant plant = new Plant(position2,2);

        earth.place(animal);
        earth.place(plant);
        earth.remove(animal);
        earth.remove(plant);

        assertFalse(earth.animalsAt(position1).get().contains(animal));
        assertFalse(earth.plants.containsKey(position2));
    }

    @Test
    void move() {
    }

    @Test
    void mover() {

    }
}