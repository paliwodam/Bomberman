package agh.ics.oop.map.elem;

import agh.ics.oop.map.Vector2d;
import agh.ics.oop.map.powerup.Ghost;

import java.util.Objects;

public class Wall {
    public boolean equals(Object other) {
        return (other instanceof Wall);
    }

    public int hashCode(){
        return Objects.hash();
    }

    public String toString() { return "wall"; }
}
