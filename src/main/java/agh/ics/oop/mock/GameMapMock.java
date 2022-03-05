package agh.ics.oop.mock;

import agh.ics.oop.map.GameMap;
import agh.ics.oop.map.Vector2d;
import agh.ics.oop.map.elem.AbstractMapElement;
import agh.ics.oop.map.elem.Chest;
import agh.ics.oop.map.elem.powerup.IPowerUp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GameMapMock extends GameMap {
    public GameMapMock(Vector2d upperLeft, Vector2d lowerRight) throws FileNotFoundException {
        super(upperLeft, lowerRight);
        removeChests();
        fillWithChests();
    }

    public void putPowerUp(IPowerUp powerUp, Vector2d position) {
        if(this.isOccupiedByMapElements(position))
            throw new IllegalArgumentException("Position is already taken");

        this.powerUps.put(position, powerUp);
    }

    private void removeChests() {
        Set<Map.Entry<Vector2d, AbstractMapElement>> set = this.mapElements.entrySet();
        set.removeIf(entry -> entry.getValue() instanceof Chest);
    }

    private void fillWithChests() throws FileNotFoundException {
        Chest chest = new Chest();
        File file = new File("C:\\Users\\Martyna\\IdeaProjects\\Bomberman\\src\\test\\resources\\chestCoordinates.txt");
        Scanner input = new Scanner(file);

        while(input.hasNext()) {
            int x = Integer.parseInt(input.next());
            int y = Integer.parseInt(input.next());
            Vector2d position = new Vector2d(x, y);
            if(position.follows(this.upperLeft) && position.precedes(this.lowerRight) && !this.isOccupiedByMapElements(position))
                this.mapElements.put(position, chest);
        }

    }
}
