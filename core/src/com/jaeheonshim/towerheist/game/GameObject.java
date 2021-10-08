package com.jaeheonshim.towerheist.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jaeheonshim.towerheist.GameWorld;
import com.jaeheonshim.towerheist.RenderItem;

public abstract class GameObject extends RenderItem {
    protected final GameWorld gameWorld;

    public GameObject(GameWorld gameWorld, int zIndex) {
        super(zIndex);
        this.gameWorld = gameWorld;
    }

    public void update(float delta) {

    }

    public abstract void draw(SpriteBatch batch);

    public void reset() {

    }
}
