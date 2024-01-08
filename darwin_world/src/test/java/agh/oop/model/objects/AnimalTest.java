package agh.oop.model.objects;

import agh.oop.model.map.Earth;
import agh.oop.model.map.MapDirection;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.inheritance.Genome;
import agh.oop.model.objects.inheritance.Mutation;
import agh.oop.model.objects.inheritance.StandardMutation;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void testIncrementChildrenCount() {
        //Given
        var genList = List.of(1, 3, 7);
        var genome = new Genome(genList, 3);
        var animal = new Animal(new Vector2d(1, 1), 1, genome, 1);

        //When
        animal.incrementChildrenCount();
        animal.incrementChildrenCount();

        //Then
        assertEquals(2, animal.getChildrenCount());
    }

    @Test
    void testEat() {
        //Given
        var genList = List.of(1, 3, 7);
        var genome = new Genome(genList, 3);
        var animal = new Animal(new Vector2d(1, 1), 1, genome, 1);
        var plant = new Plant(new Vector2d(1, 1), 3,false);

        //When
        animal.eat(plant);

        //Then
        assertEquals(4, animal.getEnergy());
    }

    @Test
    void testReproduce() {
        //Given
        var genList1 = List.of(1, 3, 7);
        var genList2 = List.of(5, 5, 6);
        var genome1 = new Genome(genList1, 3);
        var genome2 = new Genome(genList2, 3);
        var animal1 = new Animal(new Vector2d(1, 1), 4, genome1, 1);
        var animal2 = new Animal(new Vector2d(1, 1), 8, genome2, 1);
        var mutation = new StandardMutation(new int[]{0, 0});

        //When
        var child = animal1.reproduce(animal2,mutation);

        //Then
        assertTrue(child.isAt(animal1.getPosition()));
        assertEquals(2, child.getEnergy());
        assertEquals(3, animal1.getEnergy());
        assertEquals(7, animal2.getEnergy());
        boolean option1 = Objects.equals(child.getGenome().getGeneList(), List.of(1, 5, 6));
        boolean option2 = Objects.equals(child.getGenome().getGeneList(), List.of(5, 5, 7));
        assertTrue(option1 || option2);
    }

    @Test
    void testSingleMove() throws NoSuchFieldException, IllegalAccessException {
        //Given
        var genList1 = List.of(1);
        var genList2 = List.of(2);
        var genList3 = List.of(5);
        var genome1 = new Genome(genList1, 1);
        var genome2 = new Genome(genList2, 1);
        var genome3 = new Genome(genList3, 1);
        var animal1 = new Animal(new Vector2d(1, 1), 4, genome1, 1);
        var animal2 = new Animal(new Vector2d(1, 0), 8, genome2, 1);
        var animal3 = new Animal(new Vector2d(1, 1), 8, genome3, 1);
        Field directionField1 = animal1.getClass().getDeclaredField("direction");
        Field directionField2 = animal2.getClass().getDeclaredField("direction");
        Field directionField3 = animal3.getClass().getDeclaredField("direction");
        directionField1.setAccessible(true);
        directionField2.setAccessible(true);
        directionField3.setAccessible(true);
        directionField1.set(animal1,MapDirection.N);
        directionField2.set(animal2,MapDirection.N);
        directionField3.set(animal3,MapDirection.N);

        var options = new Earth(2,2);

        //When
        animal1.move(options);
        animal2.move(options);
        animal3.move(options);

        //Then
        assertTrue(animal1.isAt(new Vector2d(1, 1)));
        assertEquals(MapDirection.SW,animal1.getDirection());
        assertTrue(animal2.isAt(new Vector2d(0, 0)));
        assertEquals(MapDirection.E,animal2.getDirection());
        assertTrue(animal3.isAt(new Vector2d(0, 0)));
        assertEquals(MapDirection.SW,animal3.getDirection());
    }

    @Test
    void testMove() throws NoSuchFieldException, IllegalAccessException {
        //Given
        var genList = List.of(2,0,6,0,1,5);
        var genome = new Genome(genList, 6);
        while(genome.getActiveGene()!=0){
            genome.nextGene();
        }
        var animal1 = new Animal(new Vector2d(2, 1), 4, genome, 1);
        Field directionField1 = animal1.getClass().getDeclaredField("direction");
        directionField1.setAccessible(true);
        directionField1.set(animal1,MapDirection.N);
        var options = new Earth(4,4);

        //When
        for(int i=0;i<6;i++){
            animal1.move(options);
        }

        //Then
        assertTrue(animal1.isAt(new Vector2d(1, 3)));
        assertEquals(MapDirection.E,animal1.getDirection());
    }
}