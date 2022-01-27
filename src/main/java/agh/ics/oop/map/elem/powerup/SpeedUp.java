package agh.ics.oop.map.elem.powerup;

import agh.ics.oop.map.Player;

import java.util.Objects;

public class SpeedUp implements IPowerUp{
    @Override
    public void activate(Player player) {
        player.spedUp();
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof SpeedUp);
    }

    @Override
    public int hashCode(){
        return Objects.hash();
    }

    @Override
    public String toString() { return "speedUp"; }
}
