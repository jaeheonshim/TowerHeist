package com.jaeheonshim.towerheist;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.function.Supplier;

public class MapRenderer extends RenderItem {
    private Supplier<OrthogonalTiledMapRenderer> renderer;

    public MapRenderer(Supplier<OrthogonalTiledMapRenderer> renderer, int z) {
        super(z);
        this.renderer = renderer;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        renderer.get().render();
    }
}