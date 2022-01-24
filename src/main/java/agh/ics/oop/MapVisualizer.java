package agh.ics.oop;

import agh.ics.oop.map.GameMap;
import agh.ics.oop.map.Vector2d;

public class MapVisualizer {
    private static final String EMPTY_CELL = " ";
    private static final String FRAME_SEGMENT = "-";
    private static final String CELL_SEGMENT = "|";
    private GameMap map;

    /**
     * Initializes the MapVisualizer with an instance of map to visualize.
     * @param map
     */
    public MapVisualizer(GameMap map) {
        this.map = map;
    }

    /**
     * Convert selected region of the map into a string. It is assumed that the
     * indices of the map will have no more than two characters (including the
     * sign).
     *
     * @param upperLeft  The lower left corner of the region that is drawn.
     * @param lowerRight The upper right corner of the region that is drawn.
     * @return String representation of the selected region of the map.
     */
    public String draw(Vector2d upperLeft, Vector2d lowerRight) {
        StringBuilder builder = new StringBuilder();
        for (int i = lowerRight.y + 1; i >= upperLeft.y - 1; i--) {
            if (i == lowerRight.y + 1) {
                builder.append(drawHeader(upperLeft, lowerRight));
            }
            builder.append(String.format("%3d: ", i));
            for (int j = upperLeft.x; j <= lowerRight.x + 1; j++) {
                if (i < upperLeft.y || i > lowerRight.y) {
                    builder.append(drawFrame(j <= lowerRight.x));
                } else {
                    builder.append(CELL_SEGMENT);
                    if (j <= lowerRight.x) {
                        builder.append(drawObject(new Vector2d(j, i)));
                    }
                }
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    private String drawFrame(boolean innerSegment) {
        if (innerSegment) {
            return FRAME_SEGMENT + FRAME_SEGMENT;
        } else {
            return FRAME_SEGMENT;
        }
    }

    private String drawHeader(Vector2d lowerLeft, Vector2d upperRight) {
        StringBuilder builder = new StringBuilder();
        builder.append(" y\\x ");
        for (int j = lowerLeft.x; j < upperRight.x + 1; j++) {
            builder.append(String.format("%2d", j));
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private String drawObject(Vector2d currentPosition) {
        String result;
        if (this.map.isOccupied(currentPosition)) {
            Object object = this.map.objectAt(currentPosition);
            if (object != null) {
                result = object.toString();
            } else {
                result = EMPTY_CELL;
            }
        } else {
            result = EMPTY_CELL;
        }
        return result;
    }
}