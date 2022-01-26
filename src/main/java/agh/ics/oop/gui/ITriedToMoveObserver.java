package agh.ics.oop.gui;

import agh.ics.oop.map.Direction;
import agh.ics.oop.map.Player;

public interface ITriedToMoveObserver {
    void tiredToMove(Player player, Direction direction);
}
