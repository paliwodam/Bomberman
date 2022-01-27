package agh.ics.oop.gui;

import agh.ics.oop.map.Direction;
import agh.ics.oop.map.GameMap;
import agh.ics.oop.map.Player;
import agh.ics.oop.map.Vector2d;
import agh.ics.oop.map.elem.Wall;
import javafx.scene.layout.*;

public class MapVisualization {

    private GridPane gridPane;

    private MapGuiElement mapElem;

    private final GameMap map;
    private final Player player1;
    private final Player player2;

    private static final int cellSize = 40;

    private static final Wall wall = new Wall();

    private final static Vector2d gridUpperLeft = new Vector2d(0, 0);
    private final static Vector2d mapUpperLeft = new Vector2d(1, 1);
    private final Vector2d gridLowerRight;
    private final Vector2d mapLowerRight;

    public MapVisualization(GameMap map, Player player1, Player player2, Vector2d mapLowerRight) {
        this.map = map;
        this.player1 = player1;
        this.player2 = player2;
        this.mapElem = new MapGuiElement(player1, player2, cellSize);
        this.mapLowerRight = mapLowerRight;
        this.gridLowerRight = mapLowerRight.add(Direction.RIGHT.tuUnitVector()).add(Direction.DOWN.tuUnitVector());
    }

    private void drawFrame() {
        this.gridPane.add(this.mapElem.getBox(wall), 0, 0);
        this.gridPane.getColumnConstraints().add(new ColumnConstraints(cellSize));
        this.gridPane.getRowConstraints().add(new RowConstraints(cellSize));

        for(int i = gridUpperLeft.x + 1; i <= this.gridLowerRight.x; i++) {
            this.gridPane.getColumnConstraints().add(new ColumnConstraints(cellSize));
            this.gridPane.add(this.mapElem.getBox(wall), i, 0);
        }

        for(int j = gridUpperLeft.y + 1; j <= this.gridLowerRight.y; j++) {
            this.gridPane.getRowConstraints().add(new RowConstraints(cellSize));
            this.gridPane.add(this.mapElem.getBox(wall), 0, j);
        }

        for(int i = gridUpperLeft.x+1; i <= this.gridLowerRight.x; i++) {
            this.gridPane.add(this.mapElem.getBox(wall), i, this.gridLowerRight.y);
        }

        for(int j = gridUpperLeft.y+1; j <= this.gridLowerRight.y; j++) {
            this.gridPane.add(this.mapElem.getBox(wall), this.gridLowerRight.x, j);
        }
    }

    private void drawObjects() {
        for(int i = mapUpperLeft.x; i <= this.mapLowerRight.x; i++) {
            for(int j = mapUpperLeft.y; j <= this.mapLowerRight.y; j++) {
                this.gridPane.add(this.mapElem.getBox(this.map.objectAt(new Vector2d(i, j))), i, j);
            }
        }
    }


    public VBox getBoard() {
        this.gridPane = new GridPane();
        drawFrame();
        drawObjects();

        VBox vBox = new VBox();
        HBox equipmentPlayer1 = this.mapElem.playerEquipmentBox(this.player1);
        HBox hBox = new HBox();
        hBox.setPrefWidth(600);
        HBox equipmentPlayer2 = this.mapElem.playerEquipmentBox(this.player2);

        HBox equipment = new HBox();
        equipment.getChildren().addAll(equipmentPlayer1,hBox, equipmentPlayer2);
        equipment.setPrefWidth(600);
        vBox.getChildren().add(equipment);
        vBox.getChildren().add(this.gridPane);
        return vBox;
    }
}
