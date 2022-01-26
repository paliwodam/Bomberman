package agh.ics.oop.map.powerup;

import agh.ics.oop.map.Player;

import java.util.Objects;

public class SpeedUp implements IPowerUp{
    @Override
    public void activate(Player player) {
        player.spedUp();
    }

    public boolean equals(Object other) {
        return (other instanceof SpeedUp);
    }

    public int hashCode(){
        return Objects.hash();
    }

    public String toString() { return "speedUp"; }
}
