package agh.ics.oop.map.elem;

import java.util.Objects;

public class Wall extends AbstractMapElement {
    public boolean equals(Object other) {
        return (other instanceof Wall);
    }

    public int hashCode(){
        return Objects.hash();
    }

    public String toString() { return "wall"; }
}
