package com.jaeheonshim.towerheist.game.physics;

import com.jaeheonshim.towerheist.game.objects.Key;

// fuck I should have used generics
public class KeyFixtureUserData extends FixtureUserData {
    private final Key key;

    public KeyFixtureUserData(Key key) {
        super(FixtureType.OBJECT, FixtureClass.KEY);
        this.key = key;
    }

    public Key getKey() {
        return key;
    }
}
