package agh.ics.oop.map.elem;

import java.util.Objects;

public class Chest {
    public Chest(){ }

    public boolean equals(Object other) {
        return (other instanceof Chest);
    }

    public int hashCode(){
        return Objects.hash();
    }

    public String toString() { return "chest"; }
}
