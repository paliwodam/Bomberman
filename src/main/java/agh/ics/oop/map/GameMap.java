package agh.ics.oop.map;

import agh.ics.oop.InitialValues;
import agh.ics.oop.gui.ITriedToMoveObeserver;
import agh.ics.oop.map.elem.Bomb;
import agh.ics.oop.map.elem.Chest;
import agh.ics.oop.map.elem.IBombExplodedObserver;
import agh.ics.oop.map.elem.Wall;
import agh.ics.oop.map.powerup.*;

import java.util.*;

public class GameMap implements ITriedToMoveObeserver, IBombExplodedObserver {
    private static final Random random = new Random();

    private static final Vector2d upperLeft = new Vector2d(0, 0);
    private final Vector2d lowerRight;
    private final Wall wall = new Wall();
    private final Map<Vector2d, Wall> walls = new LinkedHashMap<>();
    private final Map<Vector2d, Chest> chests = new LinkedHashMap<>();
    private final Map<Vector2d, IPowerUp> powerUps = new LinkedHashMap<>();
    private final Map<Vector2d, Bomb> bombs = new LinkedHashMap<>();
    private final Map<Player, Vector2d> playersPositions = new LinkedHashMap<>();

    private final static Direction[] bombRange = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};


    public GameMap(InitialValues values) {
        this.lowerRight = new Vector2d(values.width-1, values.height-1);
        this.playersPositions.put(new Player(this, Direction.DOWN), upperLeft);
        this.playersPositions.put(new Player(this, Direction.UP), lowerRight);
    }

    private void fillWithWalls() {
        // TODO
    }

    private void fillWithChests() {

    }

    public Object objectAt(Vector2d position) {
        Object object = this.walls.get(position);
        if(object != null)
            return object;
        object = this.chests.get(position);
        if(object != null)
            return object;
        object = this.bombs.get(position);
        if(object != null)
            return object;
        return playerAt(position);
    }

    public boolean isOccupied(Vector2d position) {
        if(this.walls.containsKey(position))
            return true;

        if(this.chests.containsKey(position))
            return true;

        if(this.bombs.containsKey(position))
            return true;

        return playerAt(position) == null;
    }

    public boolean canMoveTo(Vector2d position) {
        if(isOccupied(position))
            return false;
        if(this.playersPositions.containsValue(position))
            return false;
        return !position.follows(upperLeft) || !position.precedes(this.lowerRight);
    }

    @Override
    public void tiredToMove(Player player, Direction direction) {
        Vector2d position = this.playersPositions.get(player).add(direction.tuUnitVector());
        if(player.isGhost() || canMoveTo(position)) {
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
    public void bombExploded(Bomb bomb) {
        Vector2d position = null;
        Set<Map.Entry<Vector2d, Bomb>> entrySet = this.bombs.entrySet();
        for (Map.Entry<Vector2d, Bomb> entry : entrySet) {
            if (entry.getKey().equals(bomb))
                position = entry.getKey();
        }
        if(position == null)
            throw new IllegalArgumentException("Bomb is not recognized by the map");

        for(Direction direction : bombRange) {
            destroyChest(position.add(direction.tuUnitVector()));
        }
        this.bombs.remove(position, bomb);
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
        IPowerUp[] availablePowerUps = {new Ghost(), new Shield(), new MovingBomb(), new Pocket(), new Speeding()};
        int n = availablePowerUps.length;
        this.powerUps.put(position, availablePowerUps[random.nextInt(n)]);
    }
}
