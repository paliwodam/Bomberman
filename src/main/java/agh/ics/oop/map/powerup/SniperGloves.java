package agh.ics.oop.map.powerup;

import agh.ics.oop.map.Player;

import java.util.Objects;

public class SniperGloves implements IPowerUp{
    @Override
    public void activate(Player player) {
        player.grabbedSniperGloves();
    }

    public boolean equals(Object other) {
        return (other instanceof SniperGloves);
    }

    public int hashCode(){
        return Objects.hash();
    }

    public String toString() { return "gloves"; }
}
