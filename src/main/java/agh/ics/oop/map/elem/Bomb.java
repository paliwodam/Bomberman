package agh.ics.oop.map.elem;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb {
    private final static Timer timer = new Timer();
    private final ArrayList<IBombExplodedObserver> observers = new ArrayList<>();

    public Bomb(){}

    public void placed(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                explode();
            }
        }, 2*1000);
    }

    private void explode(){
        for(IBombExplodedObserver observer : this.observers){
            observer.bombExploded(this);
        }
    }

    public void addIBombExplodedObserver(IBombExplodedObserver observer) {
        this.observers.add(observer);
    }

    public void removeIBombExplodedObserver(IBombExplodedObserver observer) {
        this.observers.remove(observer);
    }
}
