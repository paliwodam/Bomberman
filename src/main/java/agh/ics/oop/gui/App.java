package agh.ics.oop.gui;

import agh.ics.oop.map.*;
import agh.ics.oop.map.elem.Wall;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class App extends Application implements IPlayerDiedObserver {
    private GameMap map;
    private GridPane gridPane;
    private Stage primaryStage;
    private Scene scene;

    private MapGuiElement mapElem;

    private Player player1;
    private Player player2;

    private final static Vector2d gridUpperLeft = new Vector2d(0, 0);
    private final static Vector2d mapUpperLeft = new Vector2d(1, 1);
    private Vector2d gridLowerRight;
    private Vector2d mapLowerRight;

    private static final Wall wall = new Wall();

    private final int cellSize = 40;

    private final ArrayList<ITriedToMoveObserver> moveObservers = new ArrayList<>();


    @Override
    public void init() {
        this.gridLowerRight = new Vector2d(14, 14);
        this.mapLowerRight = new Vector2d(13, 13);

        this.map = new GameMap(mapUpperLeft, this.mapLowerRight);
        this.moveObservers.add(this.map);

        this.player1 = new Player(this.map, Direction.DOWN);
        this.player1.addIPlayerDiedObserver(this);
        this.moveObservers.add(this.player1);

        this.player2 = new Player(this.map, Direction.UP);
        this.player2.addIPlayerDiedObserver(this);
        this.moveObservers.add(this.player2);

        this.map.addPlayers(this.player1, this.player2);

        this.mapElem = new MapGuiElement(this.player1, this.player2, cellSize);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        setScene();
        primaryStage.setOnCloseRequest(event -> {
            this.primaryStage.close();
            System.exit(0);
        });
    }

    private void triedToMove(Player player, Direction direction) {
        for(ITriedToMoveObserver observer : this.moveObservers){
            observer.tiredToMove(player, direction);
        }
    }

    public void setScene() {
        Platform.runLater(()->{
            VBox vBox = gameWindow();
            this.scene = new Scene(vBox, 600, 640);
            this.primaryStage.setScene(this.scene);
            this.primaryStage.show();
            keyboardHandlers();
        });
    }

    private void drawFrame() {
        this.gridPane.add(this.mapElem.getBox(wall), 0, 0);
        this.gridPane.getColumnConstraints().add(new ColumnConstraints(this.cellSize));
        this.gridPane.getRowConstraints().add(new RowConstraints(this.cellSize));

        for(int i = gridUpperLeft.x + 1; i <= this.gridLowerRight.x; i++) {
            this.gridPane.getColumnConstraints().add(new ColumnConstraints(this.cellSize));
            this.gridPane.add(this.mapElem.getBox(wall), i, 0);
        }

        for(int j = gridUpperLeft.y + 1; j <= this.gridLowerRight.y; j++) {
            this.gridPane.getRowConstraints().add(new RowConstraints(this.cellSize));
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

    private VBox gameWindow() {
        this.gridPane = new GridPane();
        drawFrame();
        drawObjects();

        VBox vBox = new VBox();
        HBox equipment = new HBox(100);

        equipment.getChildren().addAll(
                this.mapElem.playerEquipmentBox(this.player1),
                this.mapElem.playerEquipmentBox(this.player2));

        vBox.getChildren().add(equipment);
        vBox.getChildren().add(this.gridPane);
        return vBox;
    }

    @Override
    public void playerDied(Player player) {
        System.exit(0);
    }

    public void keyboardHandlers() {
        this.scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP -> triedToMove(player2, Direction.UP);
                case DOWN -> triedToMove(player2, Direction.DOWN);
                case LEFT -> triedToMove(player2, Direction.LEFT);
                case RIGHT -> triedToMove(player2, Direction.RIGHT);

                case M -> player2.triedToPutBomb();

                case W -> triedToMove(player1, Direction.UP);
                case S -> triedToMove(player1, Direction.DOWN);
                case A -> triedToMove(player1, Direction.LEFT);
                case D -> triedToMove(player1, Direction.RIGHT);

                case C -> player1.triedToPutBomb();
            }
        });
        setScene();
    }
}
