package com.jaeheonshim.towerheist.game.physics;

import com.jaeheonshim.towerheist.game.objects.Bullet;

public class BulletFixtureUserData extends FixtureUserData {
    private final Bullet bullet;

    public BulletFixtureUserData(Bullet bullet) {
        super(FixtureType.DEATH);

        this.bullet = bullet;
    }

    public Bullet getBullet() {
        return bullet;
    }
}
