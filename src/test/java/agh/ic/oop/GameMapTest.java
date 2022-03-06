package agh.ic.oop;

import agh.ics.oop.map.Direction;
import agh.ics.oop.map.GameMap;
import agh.ics.oop.map.Player;
import agh.ics.oop.map.Vector2d;
import agh.ics.oop.map.elem.Bomb;
import agh.ics.oop.map.elem.Chest;
import agh.ics.oop.map.elem.Wall;
import agh.ics.oop.map.elem.powerup.*;
import agh.ics.oop.mock.GameMapMock;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class GameMapTest {
    @Test
    public void testFillWithWalls() {
        int min_ = 1;
        int max_ = 13;

        Vector2d upperLeft = new Vector2d(min_, min_);
        Vector2d lowerRight = new Vector2d(max_, max_);

        Wall wall = new Wall();

        GameMap gameMap = new GameMap(upperLeft, lowerRight);

        for(int i = min_; i <= max_; i++) {
            for(int j = min_; j <= max_; j++) {
                Object obj = gameMap.objectAt(new Vector2d(i, j));
                if(i % 2 == 0 && j % 2 == 0)
                    assertEquals(wall, obj);
                else
                    assertNotEquals(wall, obj);
            }
        }

        min_ = 0;
        max_ = 14;

        upperLeft = new Vector2d(min_, min_);
        lowerRight = new Vector2d(max_, max_);

        gameMap = new GameMap(upperLeft, lowerRight);

        for(int i = min_; i <= max_; i++) {
            for(int j = min_; j <= max_; j++) {
                Object obj = gameMap.objectAt(new Vector2d(i, j));
                if(i % 2 == 1 && j % 2 == 1)
                    assertEquals(wall, obj);
                else
                    assertNotEquals(wall, obj);
            }
        }
    }

    @Test
    public void testFillWithChests() {
        int min_ = 1;
        int max_ = 13;

        Vector2d upperLeft = new Vector2d(min_, min_);
        Vector2d lowerRight = new Vector2d(max_, max_);

        Chest chest = new Chest();

        GameMap gameMap = new GameMap(upperLeft, lowerRight);

        int chestNum = 0;
        for(int i = min_; i <= max_; i++) {
            for (int j = min_; j <= max_; j++) {
                Object obj = gameMap.objectAt(new Vector2d(i, j));

                if(chest.equals(obj))
                    chestNum += 1;

                if(i % 2 == 0 && j % 2 == 0)
                    assertNotEquals(chest, obj);
            }
        }

        assertEquals(GameMap.chestsNum, chestNum);

        assertNotEquals(chest, gameMap.objectAt(upperLeft));
        assertNotEquals(chest, gameMap.objectAt(upperLeft.add(Direction.RIGHT.tuUnitVector())));
        assertNotEquals(chest, gameMap.objectAt(upperLeft.add(Direction.DOWN.tuUnitVector())));

        assertNotEquals(chest, gameMap.objectAt(lowerRight));
        assertNotEquals(chest, gameMap.objectAt(lowerRight.add(Direction.LEFT.tuUnitVector())));
        assertNotEquals(chest, gameMap.objectAt(lowerRight.add(Direction.UP.tuUnitVector())));


        min_ = 0;
        max_ = 14;

        upperLeft = new Vector2d(min_, min_);
        lowerRight = new Vector2d(max_, max_);

        chest = new Chest();

        gameMap = new GameMap(upperLeft, lowerRight);

        chestNum = 0;
        for(int i = min_; i <= max_; i++) {
            for (int j = min_; j <= max_; j++) {
                Object obj = gameMap.objectAt(new Vector2d(i, j));

                if(chest.equals(obj))
                    chestNum += 1;

                if(i % 2 == 1 && j % 2 == 1)
                    assertNotEquals(chest, obj);
            }
        }

        assertEquals(GameMap.chestsNum, chestNum);

        assertNotEquals(chest, gameMap.objectAt(upperLeft));
        assertNotEquals(chest, gameMap.objectAt(upperLeft.add(Direction.RIGHT.tuUnitVector())));
        assertNotEquals(chest, gameMap.objectAt(upperLeft.add(Direction.DOWN.tuUnitVector())));

        assertNotEquals(chest, gameMap.objectAt(lowerRight));
        assertNotEquals(chest, gameMap.objectAt(lowerRight.add(Direction.LEFT.tuUnitVector())));
        assertNotEquals(chest, gameMap.objectAt(lowerRight.add(Direction.UP.tuUnitVector())));
    }

    @Test
    public void testObjectAt() {
        int min_ = 1;
        int max_ = 13;

        Vector2d upperLeft = new Vector2d(min_, min_);
        Vector2d lowerRight = new Vector2d(max_, max_);

        GameMapMock gameMap = null;
        try {
            gameMap = new GameMapMock(upperLeft, lowerRight);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }

        assert gameMap != null;

        Player player1 = new Player(gameMap);
        Player player2 = new Player(gameMap);
        gameMap.addPlayers(player1, player2);

        IPowerUp ghost = new Ghost();
        IPowerUp pocket = new Pocket();
        IPowerUp shield = new Shield();
        IPowerUp sniperGloves = new SniperGloves();
        IPowerUp speedUp = new SpeedUp();

        Chest chest = new Chest();
        Wall wall = new Wall();

        Bomb bomb = new Bomb();

        gameMap.putBomb(player1, bomb);
        gameMap.putBomb(player2, bomb);
        gameMap.putPowerUp(ghost, new Vector2d(7, 7));
        gameMap.putPowerUp(pocket, new Vector2d(4, 11));
        gameMap.putPowerUp(shield, new Vector2d(5, 10));
        gameMap.putPowerUp(sniperGloves, new Vector2d(3, 6));
        gameMap.putPowerUp(speedUp, new Vector2d(8, 7));

        gameMap.tiredToMove(player1, Direction.RIGHT);

        //players and bombs
        assertEquals(player1, gameMap.objectAt(upperLeft.add(Direction.RIGHT.tuUnitVector())));
        assertEquals(bomb, gameMap.objectAt(upperLeft));
        assertEquals(player2, gameMap.objectAt(lowerRight));
        assertNotEquals(bomb, gameMap.objectAt(lowerRight));

        //powerUps
        assertEquals(ghost, gameMap.objectAt(new Vector2d(7, 7)));
        assertEquals(pocket, gameMap.objectAt(new Vector2d(4, 11)));
        assertEquals(shield, gameMap.objectAt(new Vector2d(5, 10)));
        assertEquals(sniperGloves, gameMap.objectAt(new Vector2d(3, 6)));
        assertEquals(speedUp, gameMap.objectAt(new Vector2d(8, 7)));

        //chests
        assertEquals(chest,  gameMap.objectAt(new Vector2d(1, 5)));
        assertEquals(chest,  gameMap.objectAt(new Vector2d(2, 7)));
        assertEquals(chest,  gameMap.objectAt(new Vector2d(5, 11)));
        assertEquals(chest,  gameMap.objectAt(new Vector2d(6, 3)));
        assertEquals(chest,  gameMap.objectAt(new Vector2d(13, 8)));

        //empty positions
        assertNull(gameMap.objectAt(new Vector2d(12, 5)));
        assertNull(gameMap.objectAt(new Vector2d(2, 13)));
        assertNull(gameMap.objectAt(new Vector2d(3, 3)));

        //players surroundings
        assertNull(gameMap.objectAt(upperLeft.add(Direction.DOWN.tuUnitVector())));
        assertNull(gameMap.objectAt(lowerRight.add(Direction.LEFT.tuUnitVector())));
        assertNull(gameMap.objectAt(lowerRight.add(Direction.UP.tuUnitVector())));

        //out of map
        assertNull(gameMap.objectAt(new Vector2d(0, 0)));
        assertNull(gameMap.objectAt(new Vector2d(0, 8)));
        assertNull(gameMap.objectAt(new Vector2d(-3, -2)));
        assertNull(gameMap.objectAt(new Vector2d(2, 0)));
        assertNull(gameMap.objectAt(new Vector2d(14, 14)));
        assertNull(gameMap.objectAt(new Vector2d(14, 12)));
        assertNull(gameMap.objectAt(new Vector2d(11, 14)));
        assertNull(gameMap.objectAt(new Vector2d(15, 16)));

        for(int i = min_; i <= max_; i++) {
            for(int j = min_; j <= max_; j++) {
                Object obj = gameMap.objectAt(new Vector2d(i, j));
                if(i % 2 == 0 && j % 2 == 0)
                    assertEquals(wall, obj);
                else
                    assertNotEquals(wall, obj);
            }
        }
    }

    @Test
    public void testIsOccupiedByMapElement() {
        int min_ = 1;
        int max_ = 13;

        Vector2d upperLeft = new Vector2d(min_, min_);
        Vector2d lowerRight = new Vector2d(max_, max_);

        GameMapMock gameMap = null;
        try {
            gameMap = new GameMapMock(upperLeft, lowerRight);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }

        assert gameMap != null;

        Player player1 = new Player(gameMap);
        Player player2 = new Player(gameMap);
        gameMap.addPlayers(player1, player2);

        IPowerUp ghost = new Ghost();
        IPowerUp pocket = new Pocket();
        IPowerUp shield = new Shield();
        IPowerUp sniperGloves = new SniperGloves();
        IPowerUp speedUp = new SpeedUp();

        Bomb bomb = new Bomb();

        gameMap.putBomb(player1, bomb);
        gameMap.putBomb(player2, bomb);

        gameMap.putBomb(player1, bomb);
        gameMap.putBomb(player2, bomb);
        gameMap.putPowerUp(ghost, new Vector2d(7, 7));
        gameMap.putPowerUp(pocket, new Vector2d(4, 11));
        gameMap.putPowerUp(shield, new Vector2d(5, 10));
        gameMap.putPowerUp(sniperGloves, new Vector2d(3, 6));
        gameMap.putPowerUp(speedUp, new Vector2d(8, 7));

        gameMap.tiredToMove(player1, Direction.RIGHT);

        for(int i = min_; i <= max_; i++) {
            for(int j = min_; j <= max_; j++) {
                if(i % 2 == 0 && j % 2 == 0)
                    assertTrue(gameMap.isOccupiedByMapElements(new Vector2d(i, j)));
            }
        }

        // bombs
        assertTrue(gameMap.isOccupiedByMapElements(upperLeft));
        assertTrue(gameMap.isOccupiedByMapElements(lowerRight));

        //chests
        assertTrue(gameMap.isOccupiedByMapElements(new Vector2d(1, 5)));
        assertTrue(gameMap.isOccupiedByMapElements(new Vector2d(2, 7)));
        assertTrue(gameMap.isOccupiedByMapElements(new Vector2d(5, 11)));
        assertTrue(gameMap.isOccupiedByMapElements(new Vector2d(6, 3)));
        assertTrue(gameMap.isOccupiedByMapElements(new Vector2d(13, 8)));

        //powerUps
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(7, 7)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(4, 11)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(5, 10)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(3, 6)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(8, 7)));

        //empty positions
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(12, 5)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(2, 13)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(3, 3)));


        //players and surroundings
        assertFalse(gameMap.isOccupiedByMapElements(upperLeft.add(Direction.RIGHT.tuUnitVector())));
        assertFalse(gameMap.isOccupiedByMapElements(upperLeft.add(Direction.DOWN.tuUnitVector())));
        assertFalse(gameMap.isOccupiedByMapElements(lowerRight.add(Direction.LEFT.tuUnitVector())));
        assertFalse(gameMap.isOccupiedByMapElements(lowerRight.add(Direction.UP.tuUnitVector())));

        //out of map
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(0, 0)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(0, 8)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(-3, -2)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(2, 0)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(14, 14)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(14, 12)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(11, 14)));
        assertFalse(gameMap.isOccupiedByMapElements(new Vector2d(15, 16)));
    }

    @Test
    public void testCanMoveTo() {
        int min_ = 1;
        int max_ = 13;

        Vector2d upperLeft = new Vector2d(min_, min_);
        Vector2d lowerRight = new Vector2d(max_, max_);

        GameMapMock gameMap = null;
        try {
            gameMap = new GameMapMock(upperLeft, lowerRight);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }

        assert gameMap != null;

        Player player1 = new Player(gameMap);
        Player player2 = new Player(gameMap);
        gameMap.addPlayers(player1, player2);

        IPowerUp ghost = new Ghost();
        IPowerUp pocket = new Pocket();
        IPowerUp shield = new Shield();
        IPowerUp sniperGloves = new SniperGloves();
        IPowerUp speedUp = new SpeedUp();

        Bomb bomb = new Bomb();

        gameMap.putBomb(player1, bomb);
        gameMap.putBomb(player2, bomb);

        gameMap.putBomb(player1, bomb);
        gameMap.putBomb(player2, bomb);
        gameMap.putPowerUp(ghost, new Vector2d(7, 7));
        gameMap.putPowerUp(pocket, new Vector2d(4, 11));
        gameMap.putPowerUp(shield, new Vector2d(5, 10));
        gameMap.putPowerUp(sniperGloves, new Vector2d(3, 6));
        gameMap.putPowerUp(speedUp, new Vector2d(8, 7));

        gameMap.tiredToMove(player1, Direction.RIGHT);
        for(int i = min_; i <= max_; i++) {
            for(int j = min_; j <= max_; j++) {
                if(i % 2 == 0 && j % 2 == 0)
                    assertFalse(gameMap.canMoveTo(new Vector2d(i, j)));
            }
        }

        // bombs
        assertFalse(gameMap.canMoveTo(upperLeft));
        assertFalse(gameMap.canMoveTo(lowerRight));

        //chests
        assertFalse(gameMap.canMoveTo(new Vector2d(1, 5)));
        assertFalse(gameMap.canMoveTo(new Vector2d(2, 7)));
        assertFalse(gameMap.canMoveTo(new Vector2d(5, 11)));
        assertFalse(gameMap.canMoveTo(new Vector2d(6, 3)));
        assertFalse(gameMap.canMoveTo(new Vector2d(13, 8)));

        //powerUps
        assertTrue(gameMap.canMoveTo(new Vector2d(7, 7)));
        assertTrue(gameMap.canMoveTo(new Vector2d(4, 11)));
        assertTrue(gameMap.canMoveTo(new Vector2d(5, 10)));
        assertTrue(gameMap.canMoveTo(new Vector2d(3, 6)));
        assertTrue(gameMap.canMoveTo(new Vector2d(8, 7)));

        //empty positions
        assertTrue(gameMap.canMoveTo(new Vector2d(12, 5)));
        assertTrue(gameMap.canMoveTo(new Vector2d(2, 13)));
        assertTrue(gameMap.canMoveTo(new Vector2d(3, 3)));

        //players
        assertFalse(gameMap.canMoveTo(lowerRight));
        assertFalse(gameMap.canMoveTo(upperLeft.add(Direction.RIGHT.tuUnitVector())));

        //players surroundings
        assertTrue(gameMap.canMoveTo(upperLeft.add(Direction.DOWN.tuUnitVector())));
        assertTrue(gameMap.canMoveTo(lowerRight.add(Direction.LEFT.tuUnitVector())));
        assertTrue(gameMap.canMoveTo(lowerRight.add(Direction.UP.tuUnitVector())));

        //out of map
        assertFalse(gameMap.canMoveTo(new Vector2d(0, 0)));
        assertFalse(gameMap.canMoveTo(new Vector2d(0, 8)));
        assertFalse(gameMap.canMoveTo(new Vector2d(-3, -2)));
        assertFalse(gameMap.canMoveTo(new Vector2d(2, 0)));
        assertFalse(gameMap.canMoveTo(new Vector2d(14, 14)));
        assertFalse(gameMap.canMoveTo(new Vector2d(14, 12)));
        assertFalse(gameMap.canMoveTo(new Vector2d(11, 14)));
        assertFalse(gameMap.canMoveTo(new Vector2d(15, 16)));
    }

    @Test
    public void testIsInsideMap() {
        int min_ = 1;
        int max_ = 13;

        Vector2d upperLeft = new Vector2d(min_, min_);
        Vector2d lowerRight = new Vector2d(max_, max_);

        GameMap gameMap = new GameMap(upperLeft, lowerRight);

        assertTrue(gameMap.isInsideMap(new Vector2d(1, 1)));
        assertTrue(gameMap.isInsideMap(new Vector2d(4, 11)));
        assertTrue(gameMap.isInsideMap(new Vector2d(13, 13)));
        assertTrue(gameMap.isInsideMap(new Vector2d(12, 13)));
        assertTrue(gameMap.isInsideMap(new Vector2d(10, 5)));
        assertTrue(gameMap.isInsideMap(new Vector2d(1, 5)));

        assertFalse(gameMap.isInsideMap(new Vector2d(0, 0)));
        assertFalse(gameMap.isInsideMap(new Vector2d(0, 8)));
        assertFalse(gameMap.isInsideMap(new Vector2d(-3, -2)));
        assertFalse(gameMap.isInsideMap(new Vector2d(2, 0)));
        assertFalse(gameMap.isInsideMap(new Vector2d(14, 14)));
        assertFalse(gameMap.isInsideMap(new Vector2d(14, 12)));
        assertFalse(gameMap.isInsideMap(new Vector2d(11, 14)));
        assertFalse(gameMap.isInsideMap(new Vector2d(15, 16)));
    }

    @Test
    public void testTriedToMove() {
        int min_ = 1;
        int max_ = 13;

        Vector2d upperLeft = new Vector2d(min_, min_);
        Vector2d lowerRight = new Vector2d(max_, max_);

        GameMapMock gameMap = null;
        try {
            gameMap = new GameMapMock(upperLeft, lowerRight);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }

        assert gameMap != null;

        Player player1 = new Player(gameMap);
        Player player2 = new Player(gameMap);
        gameMap.addPlayers(player1, player2);

        Vector2d position1 = upperLeft;
        Vector2d position2 = lowerRight;
        gameMap.tiredToMove(player1, Direction.UP);
        assertNull(gameMap.objectAt(position1.add(Direction.UP.tuUnitVector())));
        assertEquals(player1, gameMap.objectAt(position1));
        assertEquals(player2, gameMap.objectAt(position2));

        position1 = position1.add(Direction.RIGHT.tuUnitVector());
        gameMap.tiredToMove(player1, Direction.RIGHT);
        assertNull(gameMap.objectAt(upperLeft));
        assertEquals(player1, gameMap.objectAt(position1));
        assertEquals(player2, gameMap.objectAt(position2));

        gameMap.tiredToMove(player1, Direction.DOWN);
        assertNull(gameMap.objectAt(upperLeft));
        assertNotEquals(player1, gameMap.objectAt(position1.add(Direction.DOWN.tuUnitVector())));
        assertEquals(player1, gameMap.objectAt(position1));
        assertEquals(player2, gameMap.objectAt(position2));

        gameMap.tiredToMove(player2, Direction.RIGHT);
        assertNull(gameMap.objectAt(position2.add(Direction.RIGHT.tuUnitVector())));
        assertEquals(player2, gameMap.objectAt(position2));
        assertEquals(player1, gameMap.objectAt(position1));

        position2 = position2.add(Direction.UP.tuUnitVector());
        gameMap.tiredToMove(player2, Direction.UP);
        assertNull(gameMap.objectAt(lowerRight));
        assertEquals(player2, gameMap.objectAt(position2));
        assertEquals(player1, gameMap.objectAt(position1));

        gameMap.tiredToMove(player2, Direction.LEFT);
        assertNull(gameMap.objectAt(lowerRight));
        assertEquals(player2, gameMap.objectAt(position2));
        assertEquals(player1, gameMap.objectAt(position1));
        assertNotEquals(player2, gameMap.objectAt(position2.add(Direction.LEFT.tuUnitVector())));

        position2 = position2.add(Direction.UP.tuUnitVector());
        gameMap.tiredToMove(player2, Direction.UP);
        assertNull(gameMap.objectAt(lowerRight));
        assertNull(gameMap.objectAt(position2.subtract(Direction.UP.tuUnitVector())));
        assertEquals(player2, gameMap.objectAt(position2));
        assertEquals(player1, gameMap.objectAt(position1));


        player1.turnedIntoGhost();

        gameMap.tiredToMove(player1, Direction.UP);
        assertNull(gameMap.objectAt(position1.add(Direction.UP.tuUnitVector())));
        assertNull(gameMap.objectAt(upperLeft));
        assertEquals(player1, gameMap.objectAt(position1));
        assertEquals(player2, gameMap.objectAt(position2));

        position1 = position1.add(Direction.DOWN.tuUnitVector());
        gameMap.tiredToMove(player1, Direction.DOWN);
        assertEquals(player1, gameMap.objectAt(position1));
        assertEquals(player2, gameMap.objectAt(position2));
        assertNull(gameMap.objectAt(position1.subtract(Direction.DOWN.tuUnitVector())));

        position1 = position1.add(Direction.DOWN.tuUnitVector());
        gameMap.tiredToMove(player1, Direction.DOWN);
        assertNotEquals(player1, gameMap.objectAt(position1.subtract(Direction.DOWN.tuUnitVector())));
        assertEquals(player1, gameMap.objectAt(position1));
        assertEquals(player2, gameMap.objectAt(position2));
    }

}
