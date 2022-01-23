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
    private final Map<Vector2d, Wall> walls = new LinkedHashMap<>();
    private final Map<Vector2d, Chest> chests = new LinkedHashMap<>();
    private final Map<Vector2d, IPowerUp> powerUps = new LinkedHashMap<>();
    private Player player1;
    private Player player2;

    public GameMap(InitialValues values) {
        this.lowerRight = new Vector2d(values.width-1, values.height-1);
    }


    private void fillFrame() {
        // TODO
    }

    private void fillWithWalls() {
        // TODO
    }

    private void fillWithChests() {

    }
}
