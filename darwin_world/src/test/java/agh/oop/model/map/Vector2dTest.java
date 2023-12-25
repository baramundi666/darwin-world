package agh.oop.model.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    
    @Test
    public void testEquals() {
        //Given

        //When
        var v1 = new Vector2d(2, 2);
        var v2 = new Vector2d(3, 2);

        //Then
        assertEquals(new Vector2d(2, 2), v1);
        assertNotEquals(v1, v2);
    }

    @Test
    public void testToString() {
        //Given
        int x = 2;
        int y = 3;

        //When
        var vector = new Vector2d(x, y);

        //Then
        assertEquals("(2, 3)", vector.toString());
    }

    @Test
    public void testAdd() {
        //Given

        //When
        var v1 = new Vector2d(3, 2);
        var v2 = new Vector2d(1, 2);
        var v3 = new Vector2d(4, 4);

        //Then
        assertEquals(v3, v1.add(v2));
        assertNotEquals(v1, v2.add(v3));
    }
}