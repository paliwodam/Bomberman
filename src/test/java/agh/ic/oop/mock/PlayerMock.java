package agh.ic.oop.mock;

import agh.ics.oop.map.GameMap;
import agh.ics.oop.map.Player;

public class PlayerMock extends Player {
    public PlayerMock(GameMap map) {
        super(map);
    }

    public int getBombsAmount() {
        return this.bombs.size();
    }
}
