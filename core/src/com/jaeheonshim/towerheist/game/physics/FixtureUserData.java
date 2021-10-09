package com.jaeheonshim.towerheist.game.physics;

public class FixtureUserData {
    private final FixtureType fixtureType;

    public FixtureUserData(FixtureType fixtureType) {
        this.fixtureType = fixtureType;
    }

    public FixtureType getFixtureType() {
        return fixtureType;
    }
}
