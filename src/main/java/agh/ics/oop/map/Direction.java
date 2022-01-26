package agh.ics.oop.map;

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    private static final Vector2d[] dVector2d = {
            new Vector2d(0, -1),
            new Vector2d(1, 0),
            new Vector2d(0, 1),
            new Vector2d(-1, 0)
    };

    public Vector2d tuUnitVector() {
        return dVector2d[this.ordinal()];
    }
}
