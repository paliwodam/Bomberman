package agh.ics.oop.map;

import agh.ics.oop.map.elem.*;
import agh.ics.oop.map.elem.powerup.*;
import java.util.*;

public class GameMap implements IBombExplodedObserver {
    private static final Random random = new Random();
    public static final int chestsNum = 90;

    protected final Vector2d upperLeft;
    protected final Vector2d lowerRight;
    private final Wall wall = new Wall();

    protected final Map<Vector2d, AbstractMapElement> mapElements = new LinkedHashMap<>();

    protected final Map<Vector2d, IPowerUp> powerUps = new LinkedHashMap<>();

    protected final Map<Player, Vector2d> playersPositions = new LinkedHashMap<>();

    private final static Direction[] bombRange = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};


    public GameMap(Vector2d upperLeft, Vector2d lowerRight) {
        if((lowerRight.x - upperLeft.x) < 2 || (lowerRight.y - upperLeft.y) < 2)
            throw new IllegalArgumentException("Incorrect map coordinates, map must be bigger");

        if((lowerRight.x - upperLeft.x) % 2 != 0 || (lowerRight.y - upperLeft.y) % 2 != 0)
            throw new IllegalArgumentException("Incorrect map coordinates, map must be symmetrical");

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
                this.mapElements.put(new Vector2d(i, j), wall);
            }
        }
    }

    private void fillWithChests() {
        int dx = this.lowerRight.x - this.upperLeft.x + 1;
        int dy = this.lowerRight.y - this.upperLeft.y + 1;

        for(int i = 0; i < chestsNum; i++) {
            Vector2d position = new Vector2d(random.nextInt(dx) + this.upperLeft.x,
                                             random.nextInt(dy) + this.upperLeft.y);
            if(isOccupiedByMapElements(position))
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
                this.mapElements.put(position, new Chest());
        }
    }

    public Object objectAt(Vector2d position) {
        Object object = playerAt(position);
        if(object != null)
            return object;
        object = this.powerUps.get(position);
        if(object != null)
            return object;
        return this.mapElements.get(position);
    }

    public boolean isOccupiedByMapElements(Vector2d position) {
        return this.mapElements.containsKey(position);
    }

    public boolean canMoveTo(Vector2d position) {
        if(isOccupiedByMapElements(position))
            return false;

        if(this.playersPositions.containsValue(position))
            return false;

        return isInsideMap(position);
    }

    public boolean isInsideMap(Vector2d position) {
        return position.follows(upperLeft) && position.precedes(this.lowerRight);
    }

    public boolean tiredToMove(Player player, Direction direction) {
        Vector2d position = this.playersPositions.get(player).add(direction.tuUnitVector());

        if(canMoveTo(position) || (player.isGhost() && isInsideMap(position))) {
            this.playersPositions.replace(player, position);

            IPowerUp powerUp = this.powerUps.get(position);
            if(powerUp != null) {
                powerUp.activate(player);
                this.powerUps.remove(position, powerUp);
            }
            return true;
        }
        return false;
    }

    public boolean putBomb(Player player, Bomb bomb) {
        Vector2d position = this.playersPositions.get(player);
        if(isOccupiedByMapElements(position))
            return false;

        this.mapElements.put(position, bomb);
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
        Player player = playerAt(position);
        if(player != null)
            player.bombReached();

        for(Direction direction : bombRange) {
            Vector2d rangePosition = position.add(direction.tuUnitVector());
            destroyChest(rangePosition);
            player = playerAt(rangePosition);
            if(player != null)
                player.bombReached();
        }
        this.mapElements.remove(position);
    }

    private void destroyChest(Vector2d position) {
        if(this.mapElements.get(position) instanceof Chest) {
            this.mapElements.remove(position);
            if(Math.random() <= 0.3) {
                generateRandomPowerUp(position);
            }
        }
    }

    private void generateRandomPowerUp(Vector2d position) {
        IPowerUp[] availablePowerUps = {new Ghost(), new Shield(), new SniperGloves(), new Pocket(), new SpeedUp()};
        int n = availablePowerUps.length;
        this.powerUps.put(position, availablePowerUps[random.nextInt(n)]);
    }

    public boolean moveBomb(Player player) {
        Vector2d displacement = player.getDirection().tuUnitVector();
        Vector2d position = this.playersPositions.get(player).add(displacement);

        if(!(this.mapElements.get(position) instanceof Bomb))
            return false;

        Bomb bomb = (Bomb) this.mapElements.get(position);
        Vector2d newPosition = position;
        for(int i = 0; i < 3; i++) {
            newPosition = newPosition.add(displacement);
        }

        while(isOccupiedByMapElements(newPosition) || playerAt(newPosition) != null) {
            newPosition = newPosition.add(displacement);
        }

        if(newPosition.precedes(this.lowerRight) && newPosition.follows(this.upperLeft)) {
            this.mapElements.remove(position, bomb);
            this.mapElements.put(newPosition, bomb);
            bomb.addPosition(newPosition);
            return true;
        }
        return false;
    }

    public void addPlayers(Player player1, Player player2) {
        this.playersPositions.put(player1, upperLeft);
        this.playersPositions.put(player2, lowerRight);
    }
}
