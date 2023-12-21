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
    public void testPlaceAnimal() {
        //Given
        var position1 = new Vector2d(2 , 1);
        var position2 = new Vector2d(1 , 0);
        var position3 = new Vector2d(3 , 1);
        var genome = new Genome(List.of(1,2,5,4), 2);
        var animal1 = new Animal(position1, 5, genome, 1);
        var animal2 = new Animal(position1, 5, genome, 1);
        var animal3 = new Animal(position2, 5, genome, 1);

        //When
        var map = new Earth(4,4);
        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        var animalMap = map.getAnimals();

        //Then
        assertTrue(animalMap.get(position1).contains(animal1));
        assertTrue(animalMap.get(position1).contains(animal2));
        assertTrue(animalMap.get(position2).contains(animal3));
        assertFalse(animalMap.containsKey(position3));
    }

    @Test
    public void testPlacePlant() {
        //Given
        var position1 = new Vector2d(2 , 1);
        var position2 = new Vector2d(3 , 2);
        var position3 = new Vector2d(5 , 1);
        var plant1 = new Plant(position1, 1, false);
        var plant2 = new Plant(position1, 1, false);
        var plant3 = new Plant(position2, 1, false);

        //When
        var map = new Earth(4,4);
        map.placePlant(plant1);
        map.placePlant(plant2);
        map.placePlant(plant3);
        var plantMap = map.getPlants();

        //Then
        assertEquals(plant2, plantMap.get(position1));
        assertEquals(plant3, plantMap.get(position2));
        assertFalse(plantMap.containsKey(position3));
    }

    @Test
    public void testRemoveAnimal() {
        //Given
        var position1 = new Vector2d(2 , 1);
        var position2 = new Vector2d(1 , 0);
        var position3 = new Vector2d(3 , 1);
        var genome = new Genome(List.of(1,2,5,4), 2);
        var animal1 = new Animal(position1, 5, genome, 1);
        var animal2 = new Animal(position1, 5, genome, 1);
        var animal3 = new Animal(position2, 5, genome, 1);
        var animal4 = new Animal(position2, 5, genome, 1);
        var animal5= new Animal(position3, 5, genome, 1);

        //When
        var map = new Earth(4,4);
        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        map.placeAnimal(animal4);
        map.placeAnimal(animal5);
        map.removeAnimal(animal1);
        map.removeAnimal(animal2);
        map.removeAnimal(animal4);
        map.removeAnimal(animal5);
        var animalMap = map.getAnimals();

        //Then
        assertFalse(animalMap.containsKey(position1));
        assertTrue(animalMap.get(position2).contains(animal3));
        assertFalse(animalMap.containsKey(position3));
    }

    @Test
    public void testRemovePlant() {
        //Given
        var position1 = new Vector2d(2 , 1);
        var position2 = new Vector2d(1 , 1);
        var position3 = new Vector2d(1 , 0);
        var plant1 = new Plant(position1, 1, false);
        var plant2 = new Plant(position2, 1, false);
        var plant3 = new Plant(position3, 1, false);

        //When
        var map = new Earth(4,4);
        map.placePlant(plant1);
        map.placePlant(plant2);
        map.placePlant(plant3);
        map.removePlant(plant2);
        map.removePlant(plant3);
        var plantMap = map.getPlants();

        //Then
        assertTrue(plantMap.containsKey(position1));
        assertFalse(plantMap.containsKey(position2));
        assertFalse(plantMap.containsKey(position3));
    }

    @Test
    public void testMove(){
        //Given
        var position1 = new Vector2d(2 , 1);
        var position2 = new Vector2d(1 , 1);
        var position3 = new Vector2d(1 , 0);
        var genome = new Genome(List.of(1,2,5,4), 2);
        var animal1 = new Animal(position1, 5, genome, 1);
        var animal2 = new Animal(position2, 5, genome, 1);
        var animal3 = new Animal(position3, 5, genome, 1);

        //When
        var map = new Earth(4,4);
        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        map.move(animal1);
        map.move(animal2);
        map.move(animal3);
        var newPosition1 = animal1.getPosition();
        var newPosition2 = animal2.getPosition();
        var newPosition3 = animal3.getPosition();
        var animalMap = map.getAnimals();

        //Then
        assertTrue(animalMap.get(newPosition1).contains(animal1));
        assertTrue(animalMap.get(newPosition2).contains(animal2));
        assertTrue(animalMap.get(newPosition3).contains(animal3));
    }

    @Test
    public void testMover() {
        //Given
        var newPosition1 = new Vector2d(2 , 4);
        var newPosition2 = new Vector2d(4 , 3);
        var newPosition3 = new Vector2d(-1 , 2);
        var newPosition4 = new Vector2d(2 , 3);


        //When
        var map = new Earth(4,4);

        //Then
        assertEquals(Optional.empty(), map.mover(newPosition1));
        assertEquals(Optional.of(new Vector2d(0, 3)), map.mover(newPosition2));
        assertEquals(Optional.of(new Vector2d(3, 2)), map.mover(newPosition3));
        assertEquals(Optional.of(newPosition4), map.mover(newPosition4));
    }
}