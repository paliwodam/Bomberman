package agh.ics.oop.mock;

import agh.ics.oop.map.GameMap;
import agh.ics.oop.map.Player;
import agh.ics.oop.map.elem.Bomb;

import java.util.Collections;
import java.util.List;

public class PlayerMock extends Player {
    public PlayerMock(GameMap map) {
        super(map);
    }

    public int getBombsAmount() {
        return this.bombs.size();
    }
}
