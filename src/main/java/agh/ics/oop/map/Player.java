package agh.ics.oop.map;

import agh.ics.oop.gui.ITriedToMoveObeserver;
import agh.ics.oop.gui.ITriedToPutBombObserver;
import agh.ics.oop.map.elem.Bomb;
import agh.ics.oop.map.elem.IBombExplodedObserver;

import java.util.LinkedList;

public class Player implements ITriedToMoveObeserver, IBombExplodedObserver, ITriedToPutBombObserver {
    private GameMap map;
    private Health health;
    private Direction direction;
    private LinkedList<Bomb> bombs = new LinkedList<>();

    private boolean isGhost;
    private boolean hasShield;
    private boolean hasSniperGloves;

    public Player(GameMap map) {
        this.map = map;
        this.health = Health.HEIGHT;
        this.direction = Direction.UP;
        this.isGhost = false;
        this.hasShield = false;
        addBomb();
    }

    public Player(GameMap map, Direction direction){
        this(map);
        this.direction = direction;
    }

    public void addBomb(){
        Bomb bomb = new Bomb();
        bomb.addIBombExplodedObserver(this);
        this.bombs.add(bomb);
    }

    @Override
    public void tiredToMove(Player player, Direction direction){
        if(player.equals(this)) {
            this.direction = direction;
        }
    }

    public boolean isGhost(){
        return this.isGhost;
    }

//    public boolean hasShield() { return this.hasShield; }

    @Override
    public void bombExploded(Bomb bomb){
        this.bombs.add(bomb);
    }

    @Override
    public void triedToPutBomb(){
        if(!this.bombs.isEmpty()){
            Bomb bomb = this.bombs.pop();
            if(!this.map.putBomb(this, bomb))
                this.bombs.add(bomb);
        }
    }

    public void bombReached() {
        if(!this.hasShield)
            this.health = this.health.decreasedHealth();
        if(this.health == Health.DEAD){
            //END GAME
        }
    }

    public void turnedIntoGhost() {
        this.isGhost = true;
    }

    public void turnedIntoHuman() {
        this.isGhost = false;
    }

    public void grabbedShield() {
        this.hasShield = true;
    }

    public void lostShield() {
        this.hasShield = false;
    }

    public void grabbedSniperGloves() {
        this.hasSniperGloves = true;
    }

}
