package com.jaeheonshim.towerheist.game.physics;

public class PlayerFixtureUserData extends FixtureUserData {
    private final ContactDirection direction;

    public PlayerFixtureUserData(ContactDirection direction) {
        super(FixtureType.PLAYER);

        this.direction = direction;
    }

    public enum ContactDirection {
        BOTTOM, LEFT, RIGHT, BODY
    }

    public ContactDirection getDirection() {
        return direction;
    }
}
