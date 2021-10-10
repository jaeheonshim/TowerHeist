package com.jaeheonshim.towerheist.game.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class RenderItem implements Comparable<RenderItem> {
    protected int zIndex;

    public RenderItem(int z) {
        this.zIndex = z;
    }

    public abstract void draw(SpriteBatch spriteBatch);

    @Override
    public int compareTo(RenderItem o) {
        return this.zIndex - o.zIndex;
    }
}
