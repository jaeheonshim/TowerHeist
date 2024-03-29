package com.jaeheonshim.towerheist.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class CannonRayCallback implements RayCastCallback {
    private boolean pathExists = true;

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        if(fixture.getUserData() instanceof FixtureUserData && ((FixtureUserData) fixture.getUserData()).getFixtureType() == FixtureType.BLOCK) {
            pathExists = false;
            return 0;
        }

        return 1;
    }

    public boolean isPathExists() {
        return pathExists;
    }
}
