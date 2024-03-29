package com.jaeheonshim.towerheist.game.physics;

import com.jaeheonshim.towerheist.game.objects.Door;

public class DoorFixtureUserData extends FixtureUserData {
    private final Door door;

    public DoorFixtureUserData(Door door) {
        super(FixtureType.OBJECT, FixtureClass.DOOR);

        this.door = door;
    }

    public Door getDoor() {
        return door;
    }
}
