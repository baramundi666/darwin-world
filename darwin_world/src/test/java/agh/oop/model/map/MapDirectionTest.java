package agh.oop.model.map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void testToVector() {
        assertEquals(new Vector2d(0, 1), MapDirection.N.toVector());
        assertEquals(new Vector2d(1, 1), MapDirection.NE.toVector());
        assertEquals(new Vector2d(1, 0), MapDirection.E.toVector());
        assertEquals(new Vector2d(1, -1), MapDirection.SE.toVector());
        assertEquals(new Vector2d(0, -1), MapDirection.S.toVector());
        assertEquals(new Vector2d(-1, -1), MapDirection.SW.toVector());
        assertEquals(new Vector2d(-1, 0), MapDirection.W.toVector());
        assertEquals(new Vector2d(-1, 1), MapDirection.NW.toVector());
    }

    @Test
    void testConvertNumber() {
        assertEquals(MapDirection.N, MapDirection.convertNumber(0));
        assertEquals(MapDirection.NE, MapDirection.convertNumber(1));
        assertEquals(MapDirection.E, MapDirection.convertNumber(2));
        assertEquals(MapDirection.SE, MapDirection.convertNumber(3));
        assertEquals(MapDirection.S, MapDirection.convertNumber(4));
        assertEquals(MapDirection.SW, MapDirection.convertNumber(5));
        assertEquals(MapDirection.W, MapDirection.convertNumber(6));
        assertEquals(MapDirection.NW, MapDirection.convertNumber(7));
        assertThrows(IllegalStateException.class, () -> MapDirection.convertNumber(8));
    }

    @Test
    void testGenerate() {
        for (int i = 0; i < 1000; i++) {
            MapDirection direction = MapDirection.generate();
            assertNotNull(direction);
        }
    }

    @Test
    void testShift() {
        assertEquals(MapDirection.N, MapDirection.N.shift(0));
        assertEquals(MapDirection.NE, MapDirection.N.shift(1));
        assertEquals(MapDirection.E, MapDirection.NE.shift(1));
        assertEquals(MapDirection.SE, MapDirection.E.shift(1));
        assertEquals(MapDirection.S, MapDirection.SE.shift(1));
        assertEquals(MapDirection.SW, MapDirection.S.shift(1));
        assertEquals(MapDirection.W, MapDirection.SW.shift(1));
        assertEquals(MapDirection.NW, MapDirection.W.shift(1));
    }
}
