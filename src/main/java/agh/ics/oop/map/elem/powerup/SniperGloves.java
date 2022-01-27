package agh.ics.oop.map.elem.powerup;

import agh.ics.oop.map.Player;

import java.util.Objects;

public class SniperGloves implements IPowerUp{
    @Override
    public void activate(Player player) {
        player.grabbedSniperGloves();
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof SniperGloves);
    }

    @Override
    public int hashCode(){
        return Objects.hash();
    }

    @Override
    public String toString() { return "gloves"; }
}
