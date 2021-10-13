package com.jaeheonshim.towerheist;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
    private AssetManager assetManager = new AssetManager();

    private static Assets instance = new Assets();

    private Assets() {}

    public void loadAssets() {
        assetManager.load("images.atlas", TextureAtlas.class);
        assetManager.load("ui/uiskin.atlas", TextureAtlas.class);
        assetManager.finishLoading();
    }

    public TextureRegion fromAtlas(String name) {
        TextureAtlas atlas = assetManager.get("images.atlas");
        TextureAtlas.AtlasRegion region = atlas.findRegion(name);

        return region;
    }

    public TextureAtlas atlas() {
        return assetManager.get("images.atlas");
    }

    public <T> T get(String s) {
        return assetManager.get(s);
    }

    public static Assets instance() {
        return instance;
    }
}
