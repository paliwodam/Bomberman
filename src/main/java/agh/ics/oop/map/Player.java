package agh.ics.oop.map;

import agh.ics.oop.map.elem.Bomb;
import agh.ics.oop.map.elem.IBombExplodedObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Player implements IBombExplodedObserver {
    private Health health;
    private Direction direction;
    private final GameMap map;
    protected final LinkedList<Bomb> bombs = new LinkedList<>();
    private final ArrayList<IPlayerDiedObserver> observers = new ArrayList<>();

    private final AtomicBoolean isGhost;
    private final AtomicBoolean hasShield;
    private final AtomicBoolean hasSniperGloves;
    private final AtomicBoolean spedUp;
    private final AtomicInteger pocketsNum;

    public Player(GameMap map) {
        this.map = map;
        this.health = Health.HEIGHT;
        this.direction = Direction.UP;
        this.isGhost = new AtomicBoolean(false);
        this.hasShield = new AtomicBoolean(false);
        this.hasSniperGloves = new AtomicBoolean(false);
        this.spedUp = new AtomicBoolean(false);
        this.pocketsNum = new AtomicInteger(0);
        addBomb();
    }

    public Player(GameMap map, Direction direction){
        this(map);
        this.direction = direction;
    }

    private void addBomb(){
        Bomb bomb = new Bomb();
        bomb.addIBombExplodedObserver(this);
        this.bombs.add(bomb);
    }

    public void tiredToMove(Direction direction){
            this.direction = direction;
    }

    public Health getHealth() { return this.health; }

    public int getPocketsNum() { return this.pocketsNum.get(); }

    public boolean isGhost(){
        return this.isGhost.get();
    }

    public boolean hasGloves() { return this.hasSniperGloves.get(); }

    public boolean hasShield() { return this.hasShield.get(); }

    public boolean hasSpeedUp() { return this.spedUp.get(); }

    public Direction getDirection() { return this.direction; }

    @Override
    public void bombExploded(Vector2d position){ addBomb();}


    public void triedToPutBomb(){
        if(this.hasSniperGloves.get() && this.map.moveBomb(this))
            return;

        if(!this.bombs.isEmpty()){
            Bomb bomb = this.bombs.pop();
            if(!this.map.putBomb(this, bomb))
                this.bombs.add(bomb);
        }
    }

    public void bombReached() {
        if(!this.hasShield.get())
            this.health = this.health.decreasedHealth();
        if(this.health == Health.DEAD){
            for(IPlayerDiedObserver observer : this.observers)
                observer.playerDied(this);
        }
    }

    public void addIPlayerDiedObserver(IPlayerDiedObserver observer) {
        this.observers.add(observer);
    }

    public void removeIPlayerDiedObserver(IPlayerDiedObserver observer) {
        this.observers.remove(observer);
    }

    public void turnedIntoGhost() {
        this.isGhost.set(true);
    }

    public void turnedIntoHuman() {
        this.isGhost.set(false);
    }

    public void grabbedShield() {
        this.hasShield.set(true);
    }

    public void lostShield() {
        this.hasShield.set(false);
    }

    public void grabbedSniperGloves() {
        this.hasSniperGloves.set(true);
    }

    public void spedUp() {
        this.spedUp.set(true);
    }

    public void grabbedPocket() {
        this.pocketsNum.getAndAdd(1);
        addBomb();
    }

    public boolean equals(Object other){
        return other == this;
    }

    public int hashCode() { return Objects.hash(); }

    public String toString() { return "player"; }
}
