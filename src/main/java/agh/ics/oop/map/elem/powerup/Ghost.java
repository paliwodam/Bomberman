package agh.ics.oop.map.elem.powerup;

import agh.ics.oop.map.Player;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Ghost implements IPowerUp{

    private final static Timer timer = new Timer();

    @Override
    public void activate(Player player) {
        player.turnedIntoGhost();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.turnedIntoHuman();
            }
        }, 7*1000);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Ghost);
    }

    @Override
    public int hashCode(){
        return Objects.hash();
    }

    @Override
    public String toString() { return "ghost"; }
}
