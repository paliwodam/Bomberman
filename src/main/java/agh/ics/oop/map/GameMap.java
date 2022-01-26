package agh.ics.oop.map;

import agh.ics.oop.gui.ITriedToMoveObserver;
import agh.ics.oop.map.elem.Bomb;
import agh.ics.oop.map.elem.Chest;
import agh.ics.oop.map.elem.IBombExplodedObserver;
import agh.ics.oop.map.elem.Wall;
import agh.ics.oop.map.powerup.*;
import java.util.*;

public class GameMap implements ITriedToMoveObserver, IBombExplodedObserver {
    private static final Random random = new Random();

    private final Vector2d upperLeft;
    private final Vector2d lowerRight;
    private final Wall wall = new Wall();
    private final Map<Vector2d, Wall> walls = new LinkedHashMap<>();
    private final Map<Vector2d, Chest> chests = new LinkedHashMap<>();
    private final Map<Vector2d, IPowerUp> powerUps = new LinkedHashMap<>();
    private final Map<Vector2d, Bomb> bombs = new LinkedHashMap<>();
    private final Map<Player, Vector2d> playersPositions = new LinkedHashMap<>();

    private final static Direction[] bombRange = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};


    public GameMap(Vector2d upperLeft, Vector2d lowerRight) {
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
        fillWithWalls();
        fillWithChests();
    }

    private void fillWithWalls() {
        Vector2d start = this.upperLeft.add(Direction.DOWN.tuUnitVector()).add(Direction.RIGHT.tuUnitVector());
        Vector2d end = this.lowerRight.add(Direction.UP.tuUnitVector()).add(Direction.LEFT.tuUnitVector());

        for(int i = start.x; i <= end.x; i+=2) {
            for(int j = start.y; j <= end.y; j+=2) {
                this.walls.put(new Vector2d(i, j), wall);
            }
        }
    }

    private void fillWithChests() {
        int dx = this.lowerRight.x - this.upperLeft.x + 2;
        int dy = this.lowerRight.y - this.upperLeft.y + 2;
        for(int i = 0; i < 90; i++) {
            System.out.println(i);
            Vector2d position = new Vector2d(random.nextInt(dx), random.nextInt(dy));
            if(isOccupied(position))
                i--;
            else if(position.equals(this.lowerRight) || position.equals(this.upperLeft))
                i--;
            else if(position.equals(this.lowerRight.add(Direction.LEFT.tuUnitVector())))
                i--;
            else if(position.equals(this.lowerRight.add(Direction.UP.tuUnitVector())))
                i--;
            else if(position.equals(this.upperLeft.add(Direction.RIGHT.tuUnitVector())))
                i--;
            else if(position.equals(this.upperLeft.add(Direction.DOWN.tuUnitVector())))
                i--;
            else
                this.chests.put(position, new Chest());
        }
    }

    public Object objectAt(Vector2d position) {
        Object object = playerAt(position);
        if(object != null)
            return object;
        object = this.walls.get(position);
        if(object != null)
            return object;
        object = this.chests.get(position);
        if(object != null)
            return object;
        object = this.bombs.get(position);
        if(object != null)
            return object;
        object = this.powerUps.get(position);
        return object;
    }

    public boolean isOccupied(Vector2d position) {
        if(this.walls.containsKey(position))
            return true;

        if(this.chests.containsKey(position))
            return true;

        if(this.bombs.containsKey(position))
            return true;

        return false;
    }

    public boolean canMoveTo(Vector2d position) {
        if(isOccupied(position))
            return false;

        if(this.playersPositions.containsValue(position))
            return false;

        return isNotOutOfMap(position);
    }

    public boolean isNotOutOfMap(Vector2d position) {
        return position.follows(upperLeft) && position.precedes(this.lowerRight);
    }

    @Override
    public void tiredToMove(Player player, Direction direction) {
        Vector2d position = this.playersPositions.get(player).add(direction.tuUnitVector());

        if(canMoveTo(position) || (player.isGhost() && isNotOutOfMap(position))) {
            this.playersPositions.replace(player, position);

            IPowerUp powerUp = this.powerUps.get(position);
            if(powerUp != null) {
                powerUp.activate(player);
                this.powerUps.remove(position, powerUp);
            }
        }
    }

    public boolean putBomb(Player player, Bomb bomb) {
        Vector2d position = this.playersPositions.get(player);
        if(isOccupied(position))
            return false;

        this.bombs.put(position, bomb);
        bomb.addPosition(position);
        bomb.addIBombExplodedObserver(this);
        bomb.placed();

        return true;
    }

    public Player playerAt(Vector2d position) {
        Set<Map.Entry<Player, Vector2d>> entrySet = this.playersPositions.entrySet();
        for (Map.Entry<Player, Vector2d> entry : entrySet) {
            if (entry.getValue().equals(position))
                return entry.getKey();
        }
        return null;
    }

    @Override
    public void bombExploded(Vector2d position) {
        for(Direction direction : bombRange) {
            destroyChest(position.add(direction.tuUnitVector()));
        }
        this.bombs.remove(position);
    }

    public void destroyChest(Vector2d position) {
        if(this.chests.containsKey(position)) {
            Player player = playerAt(position);
            if(player != null) {
                player.bombReached();
            }
            this.chests.remove(position);
            if(Math.random() <= 0.3) {
                generateRandomPowerUp(position);
            }
        }
    }

    public void generateRandomPowerUp(Vector2d position) {
        IPowerUp[] availablePowerUps = {new Ghost(), new Shield(), new SniperGloves(), new Pocket(), new SpeedUp()};
        int n = availablePowerUps.length;
        this.powerUps.put(position, availablePowerUps[random.nextInt(n)]);
    }

    public void moveBomb(Player player) {
        Vector2d displacement = player.getDirection().tuUnitVector();
        Vector2d bombPosition = this.playersPositions.get(player).add(displacement);

        Bomb bomb = this.bombs.get(bombPosition);
        if(bomb == null)
            return;

        Vector2d newBombPosition = bombPosition;
        for(int i = 0; i < 3; i++) {
            newBombPosition = newBombPosition.add(displacement);
        }

        while(isOccupied(newBombPosition) || playerAt(newBombPosition) != null) {
            newBombPosition = newBombPosition.add(displacement);
        }

        if(newBombPosition.precedes(this.lowerRight)) {
            this.bombs.remove(bombPosition, bomb);
            this.bombs.put(newBombPosition, bomb);
        }
    }

    public void addPlayers(Player player1, Player player2) {
        this.playersPositions.put(player1, upperLeft);
        this.playersPositions.put(player2, lowerRight);
    }
}
