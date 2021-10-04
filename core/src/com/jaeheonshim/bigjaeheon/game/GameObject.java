package com.jaeheonshim.bigjaeheon.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jaeheonshim.bigjaeheon.GameWorld;

public abstract class GameObject {
    protected final GameWorld gameWorld;

    public GameObject(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void update(float delta) {

    }

    public abstract void draw(SpriteBatch batch);
}
