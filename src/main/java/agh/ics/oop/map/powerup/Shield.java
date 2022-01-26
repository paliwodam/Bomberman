package agh.ics.oop.map.powerup;

import agh.ics.oop.map.Player;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Shield implements IPowerUp{
    private final static Timer timer = new Timer();
    @Override
    public void activate(Player player) {
        player.grabbedShield();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.lostShield();
            }
        }, 7*1000);
    }

    public boolean equals(Object other) {
        return (other instanceof Shield);
    }

    public int hashCode(){
        return Objects.hash();
    }

    public String toString() { return "shield"; }
}
