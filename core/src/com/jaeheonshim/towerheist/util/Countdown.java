package com.jaeheonshim.towerheist.util;

public class Countdown {
    private float duration;
    private float timer;

    public Countdown(float duration) {
        this.duration = duration;
        this.timer = timer;
    }

    public void update(float delta) {
        timer -= delta;
    }

    public boolean isFinished() {
        return timer <= 0;
    }

    public void reset() {
        this.timer = duration;
    }
}
