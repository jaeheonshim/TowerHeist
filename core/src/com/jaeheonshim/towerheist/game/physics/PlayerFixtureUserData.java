package com.jaeheonshim.towerheist.game.physics;

import com.badlogic.gdx.physics.box2d.Fixture;

public class PlayerFixtureUserData extends FixtureUserData {
    private final ContactDirection direction;
    private Fixture currentBlock;

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

    public Fixture getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(Fixture currentBlock) {
        this.currentBlock = currentBlock;
    }
}
