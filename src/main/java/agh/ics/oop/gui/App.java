package agh.ics.oop.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import agh.ics.oop.map.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class App extends Application implements IPlayerDiedObserver {
    private Stage primaryStage;
    private Scene scene;

    private Player player1;
    private Player player2;


    private HashMap<Player, LocalDateTime> lastMoveTime = new LinkedHashMap<>();
    private HashMap<Player, Integer> coolDownTime = new LinkedHashMap<>();

    private MapVisualization mapVisualization;

    private final ArrayList<ITriedToMoveObserver> moveObservers = new ArrayList<>();
    private final static Vector2d mapUpperLeft = new Vector2d(1, 1);

    @Override
    public void init() {
        Vector2d mapLowerRight = new Vector2d(13, 13);

        GameMap map = new GameMap(mapUpperLeft, mapLowerRight);
        this.moveObservers.add(map);

        this.player1 = new Player(map, Direction.DOWN);
        this.player1.addIPlayerDiedObserver(this);
        this.moveObservers.add(this.player1);

        this.player2 = new Player(map, Direction.UP);
        this.player2.addIPlayerDiedObserver(this);
        this.moveObservers.add(this.player2);

        this.mapVisualization = new MapVisualization(map, player1, player2, mapLowerRight);
        map.addPlayers(this.player1, this.player2);

        this.lastMoveTime.put(player1, LocalDateTime.now());
        this.lastMoveTime.put(player2, LocalDateTime.now());
        this.coolDownTime.put(player1, 0);
        this.coolDownTime.put(player2, 0);
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

    @Override
    public void playerDied(Player player) {
        System.exit(0);
    }

    private void triedToMove(Player player, Direction direction) {
        if(this.lastMoveTime.get(player).plusSeconds(this.coolDownTime.get(player)).compareTo(LocalDateTime.now()) <= 0) {
            for (ITriedToMoveObserver observer : this.moveObservers) {
                observer.tiredToMove(player, direction);
            }
            this.lastMoveTime.replace(player, LocalDateTime.now());
        }
    }

    public void setScene() {
        Platform.runLater(()->{
            VBox vBox = this.mapVisualization.getBoard();
            this.scene = new Scene(vBox, 600, 630);
            this.primaryStage.setScene(this.scene);
            this.primaryStage.show();
            keyboardHandlers();
        });
    }


    public void keyboardHandlers() {
        this.scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {

                case M -> player2.triedToPutBomb();

                case W -> triedToMove(player1, Direction.UP);
                case S -> triedToMove(player1, Direction.DOWN);
                case A -> triedToMove(player1, Direction.LEFT);
                case D -> triedToMove(player1, Direction.RIGHT);

                case UP -> triedToMove(player2, Direction.UP);
                case DOWN -> triedToMove(player2, Direction.DOWN);
                case LEFT -> triedToMove(player2, Direction.LEFT);
                case RIGHT -> triedToMove(player2, Direction.RIGHT);

                case C -> player1.triedToPutBomb();
            }
        });
        setScene();
    }
}
