package agh.ics.oop.map.powerup;

import agh.ics.oop.map.Player;

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
}
