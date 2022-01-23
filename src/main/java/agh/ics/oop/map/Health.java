package agh.ics.oop.map;

public enum Health {
    LOW (1),
    MEDIUM (2),
    HEIGHT (3);

    public final int idx;

    Health(int i) {
        this.idx = i;
    }
}
