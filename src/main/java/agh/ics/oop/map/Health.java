package agh.ics.oop.map;

public enum Health {
    DEAD(0),
    LOW (1),
    MEDIUM (2),
    HEIGHT (3);

    public final int idx;

    Health(int i) {
        this.idx = i;
    }

    public Health decreasedHealth() {
        if(this == DEAD)
            return DEAD;
        Health[] health = Health.values();
        return health[this.ordinal()-1];
    }
}
