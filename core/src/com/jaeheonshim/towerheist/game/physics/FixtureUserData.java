package com.jaeheonshim.towerheist.game.physics;

public class FixtureUserData {
    private final FixtureType fixtureType;
    private FixtureClass fixtureClass;

    public FixtureUserData(FixtureType fixtureType) {
        this.fixtureType = fixtureType;
    }

    public FixtureUserData(FixtureType fixtureType, FixtureClass fixtureClass) {
        this.fixtureType = fixtureType;
        this.fixtureClass = fixtureClass;
    }

    public FixtureType getFixtureType() {
        return fixtureType;
    }

    public FixtureClass getFixtureClass() {
        return fixtureClass;
    }
}
