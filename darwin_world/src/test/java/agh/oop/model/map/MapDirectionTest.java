package agh.oop.model.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapDirectionTest {

    @Test
    public void testToVector() {
        //Given
        var north = MapDirection.N;
        var southEast = MapDirection.SE;
        var northWest = MapDirection.NW;
        var east = MapDirection.E;

        //When
        var v1 = new Vector2d(0, 1);
        var v2 = new Vector2d(1, -1);
        var v3 = new Vector2d(-1, 1);
        var v4 = new Vector2d(1, 0);

        //Then
        assertEquals(v1, north.toVector());
        assertEquals(v2, southEast.toVector());
        assertEquals(v3, northWest.toVector());
        assertEquals(v4, east.toVector());
        assertNotEquals(v1, east.toVector());
        assertNotEquals(v2, northWest.toVector());
    }

    @Test
    public void testShift() {
        //Given
        var north = MapDirection.N;
        var southEast = MapDirection.SE;
        var northWest = MapDirection.NW;
        var east = MapDirection.E;


        //When
        int gene1 = 5;
        int gene2 = 0;
        int gene3 = 7;

        //Then
        assertEquals(MapDirection.N, north.shift(gene2));
        assertEquals(MapDirection.NE, east.shift(gene3));
        assertEquals(MapDirection.N, southEast.shift(gene1));
        assertEquals(MapDirection.W, northWest.shift(gene3));
        assertNotEquals(MapDirection.N, north.shift(7));
        assertNotEquals(MapDirection.W, east.shift(0));
    }
}