package agh.ics.oop.map;

import agh.ics.oop.InitialValues;
import agh.ics.oop.map.elem.Chest;
import agh.ics.oop.map.elem.Wall;
import agh.ics.oop.map.powerup.IPowerUp;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameMap {

    private static final Vector2d upperLeft = new Vector2d(0, 0);
    private final Vector2d lowerRight;
    private final Wall wall = new Wall();
    private final Map<Vector2d, Wall> walls = new LinkedHashMap<>();
    private final Map<Vector2d, Chest> chests = new LinkedHashMap<>();
    private final Map<Vector2d, IPowerUp> powerUps = new LinkedHashMap<>();
    private final Map<Player, Vector2d> playersPositions = new LinkedHashMap<>();
    private Player player1;
    private Player player2;

    public GameMap(InitialValues values) {
        this.lowerRight = new Vector2d(values.width-1, values.height-1);
    }


    private void fillFrame() {
        for(int i = upperLeft.x; i <= this.lowerRight.x; i++) {
            for(int j = upperLeft.y; j <= this.lowerRight.y; j++) {
                this.walls.put(new Vector2d(i, j), this.wall);
            }
        }
    }

    private void fillWithWalls() {
        // TODO
    }

    private void fillWithChests() {

    }

    private boolean canMoveTo(Vector2d position) {
        return false;
    }

    private void move(Player player, Direction direction) {
        Vector2d position = this.playersPositions.get(player).add(direction.tuUnitVector());
        if(canMoveTo(position))
            this.playersPositions.replace(player, position);
    }
}
