package agh.ic.oop;

import agh.ics.oop.map.*;
import agh.ics.oop.map.elem.Bomb;
import agh.ics.oop.map.elem.powerup.Ghost;
import agh.ics.oop.map.elem.powerup.Pocket;
import agh.ics.oop.map.elem.powerup.Shield;
import agh.ics.oop.map.elem.powerup.SniperGloves;
import agh.ic.oop.mock.GameMapMock;
import agh.ic.oop.mock.PlayerMock;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;


public class PowerUpsTest {
    @Test
    public void testGhost() throws InterruptedException {
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

        gameMap.tiredToMove(player1, Direction.DOWN);
        Vector2d position1 = upperLeft.add(Direction.DOWN.tuUnitVector());

        gameMap.tiredToMove(player1, Direction.RIGHT);
        assertEquals(player1, gameMap.objectAt(position1));
        assertNotEquals(player1, gameMap.objectAt(position1.add(Direction.RIGHT.tuUnitVector())));

        Ghost ghost = new Ghost();
        ghost.activate(player1);

        gameMap.tiredToMove(player1, Direction.RIGHT);
        position1 = position1.add(Direction.RIGHT.tuUnitVector());
        assertEquals(player1, gameMap.objectAt(position1));

        gameMap.tiredToMove(player1, Direction.RIGHT);
        position1 = position1.add(Direction.RIGHT.tuUnitVector());
        assertEquals(player1, gameMap.playerAt(position1));

        gameMap.tiredToMove(player1, Direction.DOWN);
        position1 = position1.add(Direction.DOWN.tuUnitVector());
        assertEquals(player1, gameMap.playerAt(position1));

        Thread.sleep(8000);


        gameMap.tiredToMove(player1, Direction.RIGHT);
        assertEquals(player1, gameMap.objectAt(position1));
        assertNotEquals(player1, gameMap.objectAt(position1.add(Direction.RIGHT.tuUnitVector())));


        gameMap.tiredToMove(player2, Direction.UP);
        Vector2d position2 = lowerRight.add(Direction.UP.tuUnitVector());
        assertEquals(player2, gameMap.objectAt(position2));
        assertNotEquals(player2, gameMap.objectAt(position2.subtract(Direction.UP.tuUnitVector())));


        gameMap.tiredToMove(player2, Direction.RIGHT);
        assertNotEquals(player2, gameMap.objectAt(position2.add(Direction.RIGHT.tuUnitVector())));
        assertEquals(player2, gameMap.objectAt(position2));

        ghost.activate(player2);

        gameMap.tiredToMove(player2, Direction.UP);
        position2 = position2.add(Direction.UP.tuUnitVector());
        assertEquals(player2, gameMap.objectAt(position2));
        assertNotEquals(player2, gameMap.objectAt(position2.subtract(Direction.UP.tuUnitVector())));

        gameMap.tiredToMove(player2, Direction.RIGHT);
        assertNotEquals(player2, gameMap.objectAt(position2.add(Direction.RIGHT.tuUnitVector())));
        assertEquals(player2, gameMap.objectAt(position2));

        Thread.sleep(8000);

        gameMap.tiredToMove(player2, Direction.RIGHT);
        assertNotEquals(player2, gameMap.objectAt(position2.add(Direction.RIGHT.tuUnitVector())));
        assertEquals(player2, gameMap.objectAt(position2));

    }

    @Test
    public void testPocket() throws InterruptedException {
        int min_ = 1;
        int max_ = 13;

        Vector2d upperLeft = new Vector2d(min_, min_);
        Vector2d lowerRight = new Vector2d(max_, max_);

        GameMap gameMap = new GameMap(upperLeft, lowerRight);

        PlayerMock player1 = new PlayerMock(gameMap);
        PlayerMock player2 = new PlayerMock(gameMap);
        gameMap.addPlayers(player1, player2);

        assertEquals(1, player1.getBombsAmount());
        player1.triedToPutBomb();
        assertEquals(0, player1.getBombsAmount());
        gameMap.tiredToMove(player1, Direction.DOWN);
        assertTrue(gameMap.objectAt(upperLeft) instanceof Bomb);

        Thread.sleep(3000);

        assertEquals(1, player1.getBombsAmount());

        Pocket pocket = new Pocket();
        pocket.activate(player1);

        assertEquals(2, player1.getBombsAmount());
        player1.triedToPutBomb();
        assertEquals(1, player1.getBombsAmount());
        gameMap.tiredToMove(player1, Direction.UP);
        player1.triedToPutBomb();
        assertEquals(0, player1.getBombsAmount());
        gameMap.tiredToMove(player1, Direction.RIGHT);
        player1.triedToPutBomb();
        assertEquals(0, player1.getBombsAmount());

        pocket.activate(player1);
        assertEquals(1, player1.getBombsAmount());
        player1.triedToPutBomb();
        assertEquals(0, player1.getBombsAmount());

        Thread.sleep(3000);
        assertEquals(3, player1.getBombsAmount());
    }

    @Test
    public void testShield() throws InterruptedException {
        int min_ = 1;
        int max_ = 13;

        Vector2d upperLeft = new Vector2d(min_, min_);
        Vector2d lowerRight = new Vector2d(max_, max_);

        GameMap gameMap = new GameMap(upperLeft, lowerRight);

        Player player1 = new Player(gameMap);
        Player player2 = new Player(gameMap);
        gameMap.addPlayers(player1, player2);

        //player 1

        assertEquals(Health.HEIGHT, player1.getHealth());
        player1.triedToPutBomb();

        Thread.sleep(3000);
        assertEquals(Health.MEDIUM, player1.getHealth());

        Shield shield = new Shield();
        shield.activate(player1);

        player1.triedToPutBomb();
        assertEquals(Health.MEDIUM, player1.getHealth());

        Thread.sleep(8000);
        player1.triedToPutBomb();

        Thread.sleep(3000);
        assertEquals(Health.LOW, player1.getHealth());

        //player 2

        assertEquals(Health.HEIGHT, player2.getHealth());
        player2.triedToPutBomb();

        Thread.sleep(3000);
        assertEquals(Health.MEDIUM, player2.getHealth());

        shield = new Shield();
        shield.activate(player2);

        player1.triedToPutBomb();
        assertEquals(Health.MEDIUM, player2.getHealth());

        Thread.sleep(8000);
        player2.triedToPutBomb();

        Thread.sleep(3000);
        assertEquals(Health.LOW, player2.getHealth());
    }

    @Test
    public void testSniperGloves() {int min_ = 1;
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

        Vector2d bombPosition = lowerRight.add(Direction.UP.tuUnitVector());
        Vector2d bombShiftedPosition = bombPosition;
        Bomb bomb = new Bomb();

        gameMap.tiredToMove(player2, Direction.UP);
        player2.triedToPutBomb();
        gameMap.tiredToMove(player2, Direction.DOWN);
        gameMap.tiredToMove(player2, Direction.UP);
        player2.triedToPutBomb();

        bombShiftedPosition = bombShiftedPosition.add(Direction.UP.tuUnitVector());
        bombShiftedPosition = bombShiftedPosition.add(Direction.UP.tuUnitVector());
        bombShiftedPosition = bombShiftedPosition.add(Direction.UP.tuUnitVector());

        assertEquals(player2, gameMap.objectAt(lowerRight));
        assertEquals(bomb, gameMap.objectAt(bombPosition));
        assertNotEquals(bomb, gameMap.objectAt(bombShiftedPosition));

        SniperGloves sniperGloves = new SniperGloves();
        sniperGloves.activate(player2);
        player2.triedToPutBomb();
        assertEquals(bomb, gameMap.objectAt(bombShiftedPosition));
        assertNotEquals(bomb, gameMap.objectAt(bombPosition));

        //TODO out of map, more than 3 blocks
    }

    @Test
    public void testSpeedUp() {
        //TODO
    }
}
