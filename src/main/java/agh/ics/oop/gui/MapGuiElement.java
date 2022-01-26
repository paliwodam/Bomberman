package agh.ics.oop.gui;

import agh.ics.oop.map.Player;
import agh.ics.oop.map.Vector2d;
import agh.ics.oop.map.elem.Bomb;
import agh.ics.oop.map.elem.Chest;
import agh.ics.oop.map.elem.Wall;
import agh.ics.oop.map.powerup.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MapGuiElement {
    private final Ghost ghost = new Ghost();
    private final Pocket pocket = new Pocket();
    private final Shield shield = new Shield();
    private final SpeedUp speedUp = new SpeedUp();
    private final SniperGloves gloves = new SniperGloves();

    private final Map<Object, Image> images = new LinkedHashMap<>();
    private Image emptyImage;
    private Image heartImage;

    private final int size;


    public MapGuiElement(Player player1, Player player2, int cellSize) {
        this.size = cellSize;
        getImages(player1, player2);
    }

    public HBox getBox(Object object) {
        HBox hBox = new HBox();
        hBox.maxHeight(size);
        hBox.minHeight(size);
        if(object == null) {
            hBox.getChildren().add(new ImageView(this.emptyImage));
        }
        else if(object instanceof Player) {
            ImageView imageView = new ImageView(this.images.get(object));
            imageView.setRotate(((Player) object).getDirection().ordinal()*90);
            hBox.getChildren().add(imageView);
        }
        else {
            hBox.getChildren().add(new ImageView(this.images.get(object)));
        }
        return hBox;
    }

    public HBox playerEquipmentBox(Player player) {
        HBox hBox = new HBox();

        for(int i = 0; i < player.getHealthPoints(); i++) {
            ImageView imageView = new ImageView(this.heartImage);
            imageView.setFitHeight(20);
            imageView.setPreserveRatio(true);
            hBox.getChildren().add(imageView);
        }

        for(int i = 0; i < player.getPocketsNum(); i++){
            ImageView imageView = new ImageView(this.images.get(this.pocket));
            imageView.setFitHeight(20);
            hBox.getChildren().add(imageView);
        }

        if(player.isGhost()){
            ImageView imageView = new ImageView(this.images.get(this.ghost));
            imageView.setFitHeight(20);
            hBox.getChildren().add(imageView);
        }

        if(player.hasGloves()) {
            ImageView imageView = new ImageView(this.images.get(this.gloves));
            imageView.setFitHeight(20);
            hBox.getChildren().add(imageView);
        }

        if(player.hasShield()) {
            ImageView imageView = new ImageView(this.images.get(this.shield));
            imageView.setFitHeight(20);
            hBox.getChildren().add(imageView);
        }

        if(player.hasSpeedUp()) {
            ImageView imageView = new ImageView(this.images.get(this.speedUp));
            imageView.setFitHeight(20);
            hBox.getChildren().add(imageView);
        }
        return hBox;
    }

    private void getImages(Player player1, Player player2) {
        final Bomb bomb = new Bomb();
        final Wall wall = new Wall();
        final Chest chest = new Chest();
        try {
            this.images.put(bomb, new Image(new FileInputStream("src/main/resources/bomb.png")));
            this.images.put(wall, new Image(new FileInputStream("src/main/resources/wall.png")));
            this.images.put(chest, new Image(new FileInputStream("src/main/resources/chest.png")));
            this.images.put(ghost, new Image(new FileInputStream("src/main/resources/ghost.png")));
            this.images.put(pocket, new Image(new FileInputStream("src/main/resources/pocket.png")));
            this.images.put(shield, new Image(new FileInputStream("src/main/resources/shield.png")));
            this.images.put(speedUp, new Image(new FileInputStream("src/main/resources/speed.png")));
            this.images.put(gloves, new Image(new FileInputStream("src/main/resources/gloves.png")));
            this.images.put(player1, new Image(new FileInputStream("src/main/resources/player1.png")));
            this.images.put(player2, new Image(new FileInputStream("src/main/resources/player2.png")));
            this.heartImage = new Image(new FileInputStream("src/main/resources/heart.png"));
            this.emptyImage = new Image(new FileInputStream("src/main/resources/empty.png"));
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
