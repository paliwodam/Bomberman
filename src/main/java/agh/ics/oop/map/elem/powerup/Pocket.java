package agh.ics.oop.map.elem.powerup;

import agh.ics.oop.map.Player;

import java.util.Objects;

public class Pocket implements IPowerUp{
    @Override
    public void activate(Player player) {
        player.grabbedPocket();
    }

    public boolean equals(Object other) {
        return (other instanceof Pocket);
    }

    public int hashCode(){
        return Objects.hash();
    }

    public String toString() { return "pocket"; }
}
