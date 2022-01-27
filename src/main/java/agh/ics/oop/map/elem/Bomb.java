package agh.ics.oop.map.elem;

import agh.ics.oop.map.Vector2d;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends AbstractMapElement {
    private Vector2d position;
    private final static Timer timer = new Timer(false);
    private final ArrayList<IBombExplodedObserver> observers = new ArrayList<>();

    public Bomb(){}

    public void placed(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for(IBombExplodedObserver observer : observers) {
                    observer.bombExploded(position);
                }
            }
        }, 2 * 1000);
    }

    public void addPosition(Vector2d position) {
        this.position = position;
    }

    public void addIBombExplodedObserver(IBombExplodedObserver observer) {
        this.observers.add(observer);
    }

    public void removeIBombExplodedObserver(IBombExplodedObserver observer) {
        this.observers.remove(observer);
    }

    public boolean equals(Object other) {
        return (other instanceof Bomb);
    }

    public int hashCode(){
        return Objects.hash();
    }

    public String toString() { return "bomb"; }
}
