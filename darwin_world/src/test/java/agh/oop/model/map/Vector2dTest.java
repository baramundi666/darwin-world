package agh.oop.model.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import agh.oop.model.map.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void testConstructorAndGetters() {
        Vector2d vector = new Vector2d(2, 3);
        assertEquals(2, vector.getX());
        assertEquals(3, vector.getY());
    }

    @Test
    void testToString() {
        Vector2d vector = new Vector2d(2, 3);
        assertEquals("(2, 3)", vector.toString());
    }

    @Test
    void testAdd() {
        Vector2d vector1 = new Vector2d(2, 3);
        Vector2d vector2 = new Vector2d(-1, 1);

        Vector2d result = vector1.add(vector2);

        assertEquals(1, result.getX());
        assertEquals(4, result.getY());
    }

    @Test
    void testFollows() {
        Vector2d vector1 = new Vector2d(2, 3);
        Vector2d vector2 = new Vector2d(1, 2);

        assertTrue(vector1.follows(vector2));
        assertFalse(vector2.follows(vector1));
    }

    @Test
    void testPrecedes() {
        Vector2d vector1 = new Vector2d(2, 3);
        Vector2d vector2 = new Vector2d(3, 4);

        assertTrue(vector1.precedes(vector2));
        assertFalse(vector2.precedes(vector1));
    }

    @Test
    void testEquals() {
        Vector2d vector1 = new Vector2d(2, 3);
        Vector2d vector2 = new Vector2d(2, 3);
        Vector2d vector3 = new Vector2d(4, 5);

        assertEquals(vector1, vector2);
        assertNotEquals(vector1, vector3);
    }

    @Test
    void testHashCode() {
        Vector2d vector1 = new Vector2d(2, 3);
        Vector2d vector2 = new Vector2d(2, 3);
        Vector2d vector3 = new Vector2d(4, 5);

        assertEquals(vector1.hashCode(), vector2.hashCode());
        assertNotEquals(vector1.hashCode(), vector3.hashCode());
    }
}
