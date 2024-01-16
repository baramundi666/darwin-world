package agh.oop.model.objects;

import agh.oop.model.map.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantTest {

    @Test
    void testConstructorAndGetters() {
        Vector2d position = new Vector2d(2, 3);
        Plant plant = new Plant(position, 10, false);

        assertEquals(position, plant.getPosition());
        assertEquals(10, plant.getEnergy());
        assertFalse(plant.isPoisonous());
    }

    @Test
    void testIsPoisonous() {
        Plant nonPoisonousPlant = new Plant(new Vector2d(2, 3), 10, false);
        Plant poisonousPlant = new Plant(new Vector2d(4, 5), 5, true);

        assertFalse(nonPoisonousPlant.isPoisonous());
        assertTrue(poisonousPlant.isPoisonous());
    }

    @Test
    void testGetId() {
        Plant plant = new Plant(new Vector2d(2, 3), 10, false);
        assertNotNull(plant.getId());
        assertNotNull(plant.getId());
    }

    @Test
    void testGetImage() {
        Plant nonPoisonousPlant = new Plant(new Vector2d(2, 3), 10, false);
        Plant poisonousPlant = new Plant(new Vector2d(4, 5), 5, true);

        assertEquals("plant.png", nonPoisonousPlant.getImage());
        assertEquals("poisonousPlant.png", poisonousPlant.getImage());
    }

    @Test
    void testGetEnergy() {
        Plant nonPoisonousPlant = new Plant(new Vector2d(2, 3), 10, false);
        Plant poisonousPlant = new Plant(new Vector2d(4, 5), 5, true);

        assertEquals(10, nonPoisonousPlant.getEnergy());
        assertEquals(-5, poisonousPlant.getEnergy());
    }

    @Test
    void testIsAt() {
        Plant plant = new Plant(new Vector2d(2, 3), 10, false);
        assertTrue(plant.isAt(new Vector2d(2, 3)));
        assertFalse(plant.isAt(new Vector2d(4, 5)));
    }

    @Test
    void testToString() {
        Plant plant = new Plant(new Vector2d(2, 3), 10, false);
        assertEquals("Plant:" + plant.getId(), plant.toString());
    }
}