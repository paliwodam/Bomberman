package agh.ic.oop;

import agh.ics.oop.map.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    @Test
    public void testEquals() {
        Vector2d position1 = new Vector2d(1,2);
        Vector2d position2 = new Vector2d(-2,1);
        Vector2d position3 = new Vector2d(1, 2);
        String string = "test";
        assertEquals(position1, position1);
        assertNotEquals(position2, position1);
        assertEquals(position3, position1);
        assertNotEquals(string, position1);
        assertNotNull(position1);
    }

    @Test
    public void testToString() {
        Vector2d position1 = new Vector2d(0, 1);
        assertEquals(position1.toString(), "(0,1)");

        Vector2d position2 = new Vector2d(4, -1);
        assertEquals(position2.toString(), "(4,-1)");
    }

    @Test
    public void testPrecedes() {
        Vector2d position1 = new Vector2d(1,2);
        Vector2d position2 = new Vector2d(-2,1);
        Vector2d position3 = new Vector2d(1, 2);
        Vector2d position4 = new Vector2d(3, 5);
        Vector2d position5 = new Vector2d(3, 2);

        assertFalse(position1.precedes(position2));
        assertTrue(position1.precedes(position3));
        assertTrue(position1.precedes(position4));
        assertFalse(position5.precedes(position3));
        assertTrue(position5.precedes(position4));
        assertFalse(position4.precedes(position2));
        assertTrue(position2.precedes(position4));
        assertFalse(position1.precedes(position2));
    }

    @Test
    public void testFollows() {
        Vector2d position1 = new Vector2d(1,2);
        Vector2d position2 = new Vector2d(-2,1);
        Vector2d position3 = new Vector2d(1, 2);
        Vector2d position4 = new Vector2d(3, 5);
        Vector2d position5 = new Vector2d(3, 2);
        assertTrue(position1.follows(position2));
        assertTrue(position1.follows(position3));
        assertFalse(position1.follows(position4));
        assertFalse(position5.follows(position4));
        assertTrue(position4.follows(position5));
        assertTrue(position4.follows(position3));
        assertTrue(position5.follows(position3));
        assertFalse(position2.follows(position1));
        assertFalse(position2.follows(position5));
    }

    @Test
    public void testUpperRight() {
        Vector2d position1 = new Vector2d(1,2);
        Vector2d position2 = new Vector2d(-2,1);
        Vector2d position3 = new Vector2d(2, 0);
        Vector2d position4 = new Vector2d(3, 5);
        assertEquals(position1.upperRight(position2), new Vector2d(1, 2));
        assertEquals(position1.upperRight(position3), new Vector2d(2, 2));
        assertEquals(position1.upperRight(position4), new Vector2d(3, 5));
    }

    @Test
    public void testLowerLeft() {
        Vector2d position1 = new Vector2d(1,2);
        Vector2d position2 = new Vector2d(-2,1);
        Vector2d position3 = new Vector2d(2, 0);
        Vector2d position4 = new Vector2d(3, 5);
        assertEquals(position1.lowerLeft(position2), new Vector2d(-2, 1));
        assertEquals(position1.lowerLeft(position3), new Vector2d(1, 0));
        assertEquals(position1.lowerLeft(position4), new Vector2d(1, 2));
    }

    @Test
    public void testAdd() {
        Vector2d position1 = new Vector2d(1,2);
        Vector2d position2 = new Vector2d(-2,1);
        Vector2d position3 = new Vector2d(2, 0);
        Vector2d position4 = new Vector2d(3, 5);
        assertEquals(position1.add(position2), new Vector2d(-1, 3));
        assertEquals(position1.add(position3), new Vector2d(3, 2));
        assertEquals(position1.add(position4), new Vector2d(4, 7));
    }

    @Test
    public void testSubtract() {
        Vector2d position1 = new Vector2d(1,2);
        Vector2d position2 = new Vector2d(-2,1);
        Vector2d position3 = new Vector2d(2, 0);
        Vector2d position4 = new Vector2d(3, 5);
        assertEquals(position1.subtract(position2), new Vector2d(3, 1));
        assertEquals(position1.subtract(position3), new Vector2d(-1, 2));
        assertEquals(position1.subtract(position4), new Vector2d(-2, -3));
    }

    @Test
    public void testOpposite(){
        Vector2d position1 = new Vector2d(1,2);
        Vector2d position2 = new Vector2d(-2,1);
        Vector2d position3 = new Vector2d(2, 0);
        assertEquals(position1.opposite(), new Vector2d(-1, -2));
        assertEquals(position2.opposite(), new Vector2d(2, -1));
        assertEquals(position3.opposite(), new Vector2d(-2, 0));
    }
}
