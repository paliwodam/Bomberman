package agh.ic.oop;

import agh.ics.oop.map.Direction;
import agh.ics.oop.map.GameMap;
import agh.ics.oop.map.Player;
import agh.ics.oop.map.Vector2d;
import agh.ics.oop.map.elem.Bomb;
import agh.ics.oop.map.elem.Chest;
import agh.ics.oop.map.elem.Wall;
import org.junit.jupiter.api.Test;

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
    public void testFillWithChest() {
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

        assertEquals(GameMap.chestNum, chestNum);

        assertNotEquals(chest, gameMap.objectAt(new Vector2d(min_, min_)));
        assertNotEquals(chest, gameMap.objectAt(new Vector2d(min_, min_ + 1)));
        assertNotEquals(chest, gameMap.objectAt(new Vector2d(min_ + 1, min_)));

        assertNotEquals(chest, gameMap.objectAt(new Vector2d(max_, max_)));
        assertNotEquals(chest, gameMap.objectAt(new Vector2d(max_ - 1, max_)));
        assertNotEquals(chest, gameMap.objectAt(new Vector2d(max_, max_ - 1)));


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

        assertEquals(GameMap.chestNum, chestNum);

        assertNotEquals(chest, gameMap.objectAt(new Vector2d(min_, min_)));
        assertNotEquals(chest, gameMap.objectAt(new Vector2d(min_ + 1, min_)));
        assertNotEquals(chest, gameMap.objectAt(new Vector2d(min_, min_ + 1)));

        assertNotEquals(chest, gameMap.objectAt(new Vector2d(max_, max_)));
        assertNotEquals(chest, gameMap.objectAt(new Vector2d(max_ - 1, max_)));
        assertNotEquals(chest, gameMap.objectAt(new Vector2d(max_, max_ - 1)));
    }

    @Test
    public void testObjectAt() {
        int min_ = 1;
        int max_ = 13;

        Vector2d upperLeft = new Vector2d(min_, min_);
        Vector2d lowerRight = new Vector2d(max_, max_);

        GameMap gameMap = new GameMap(upperLeft, lowerRight);

        Player player1 = new Player(gameMap);
        Player player2 = new Player(gameMap);
        gameMap.addPlayers(player1, player2);

        Wall wall = new Wall();
        Bomb bomb = new Bomb();
        gameMap.putBomb(player1, bomb);

        gameMap.tiredToMove(player1, Direction.RIGHT);

        assertEquals(player1, gameMap.objectAt(new Vector2d(min_, min_).add(Direction.RIGHT.tuUnitVector())));
        assertEquals(bomb, gameMap.objectAt(new Vector2d(min_, min_)));
        assertEquals(player2, gameMap.objectAt(new Vector2d(max_, max_)));

        assertNull(gameMap.objectAt(new Vector2d(min_, min_).subtract(Direction.DOWN.tuUnitVector())));
        assertNull(gameMap.objectAt(new Vector2d(max_-1, max_)));
        assertNull(gameMap.objectAt(new Vector2d(max_, max_-1)));

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
}
