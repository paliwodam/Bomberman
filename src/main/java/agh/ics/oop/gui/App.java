package agh.ics.oop.gui;

import java.time.LocalDateTime;

import agh.ics.oop.map.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.temporal.ChronoUnit;
import java.util.*;

public class App extends Application implements IPlayerDiedObserver {
    private Stage primaryStage;
    private Scene scene;

    private GameMap map;

    private Player player1;
    private Player player2;

    private final Queue<Direction> movesPlayer1 = new LinkedList<>();
    private final Queue<Direction> movesPlayer2 = new LinkedList<>();

    private final HashMap<Player, LocalDateTime> lastMoveTime = new LinkedHashMap<>();
    private final HashMap<Player, Long> coolDownTime = new LinkedHashMap<>();

    private MapVisualization mapVisualization;

    private final static Vector2d mapUpperLeft = new Vector2d(1, 1);

    @Override
    public void init() {
        Vector2d mapLowerRight = new Vector2d(13, 13);

        map = new GameMap(mapUpperLeft, mapLowerRight);

        this.player1 = new Player(map, Direction.DOWN);
        this.player1.addIPlayerDiedObserver(this);

        this.player2 = new Player(map, Direction.UP);
        this.player2.addIPlayerDiedObserver(this);

        this.mapVisualization = new MapVisualization(map, player1, player2, mapLowerRight);
        map.addPlayers(this.player1, this.player2);

        this.lastMoveTime.put(player1, LocalDateTime.now());
        this.lastMoveTime.put(player2, LocalDateTime.now());
        this.coolDownTime.put(player1, 100000000L);
        this.coolDownTime.put(player2, 100000000L);
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
        player.tiredToMove(direction);
        if(this.map.tiredToMove(player, direction))
            this.lastMoveTime.replace(player, LocalDateTime.now());
    }

    private void makeMovements() {
        if(!this.movesPlayer1.isEmpty()) {
            long nanos = ChronoUnit.NANOS.between(this.lastMoveTime.get(this.player1), LocalDateTime.now());
            if(nanos >= this.coolDownTime.get(this.player1) || this.player1.hasSpeedUp())
                triedToMove(player1, this.movesPlayer1.poll());
        }
        if(!this.movesPlayer2.isEmpty()) {
            long nanos = ChronoUnit.NANOS.between(this.lastMoveTime.get(this.player2), LocalDateTime.now());
            if(nanos >= this.coolDownTime.get(this.player2) || this.player2.hasSpeedUp())
                triedToMove(player2, this.movesPlayer2.poll());
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

                case W -> movesPlayer1.add(Direction.UP);
                case S -> movesPlayer1.add(Direction.DOWN);
                case A -> movesPlayer1.add(Direction.LEFT);
                case D -> movesPlayer1.add(Direction.RIGHT);

                case UP -> movesPlayer2.add(Direction.UP);
                case DOWN -> movesPlayer2.add(Direction.DOWN);
                case LEFT -> movesPlayer2.add(Direction.LEFT);
                case RIGHT -> movesPlayer2.add(Direction.RIGHT);

                case C -> player1.triedToPutBomb();
            }
        });
        makeMovements();
        setScene();
    }
}
