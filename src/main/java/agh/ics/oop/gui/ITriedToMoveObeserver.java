package agh.ics.oop.gui;

import agh.ics.oop.map.Direction;
import agh.ics.oop.map.Player;

public interface ITriedToMoveObeserver {
    void tiredToMove(Player player, Direction direction);
}
