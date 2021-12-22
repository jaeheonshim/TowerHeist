package com.jaeheonshim.towerheist.game.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jaeheonshim.towerheist.Assets;

public class BackgroundRenderer {
    private TextureRegion skyRegion;

    public Viewport backgroundViewport;

    public BackgroundRenderer() {
        backgroundViewport = new FillViewport(1000, 1000);

        skyRegion = Assets.instance().fromAtlas("backdrop/tic-tac-toe");
    }

    public void draw(SpriteBatch spriteBatch) {
        backgroundViewport.apply();
        spriteBatch.setProjectionMatrix(backgroundViewport.getCamera().combined);

        spriteBatch.begin();
        for(int i = 0; i <= backgroundViewport.getWorldWidth() / skyRegion.getRegionWidth(); i++) {
            for(int j = 0; j <= backgroundViewport.getWorldHeight() / skyRegion.getRegionHeight(); j++) {
                spriteBatch.draw(skyRegion, i * skyRegion.getRegionWidth(), j * skyRegion.getRegionHeight());
            }
        }
        spriteBatch.end();
    }

    public void resize(int width, int height) {
        backgroundViewport.update(width, height, true);
    }
}
