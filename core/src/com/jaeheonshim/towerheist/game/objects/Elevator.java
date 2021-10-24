package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.jaeheonshim.towerheist.Assets;
import com.jaeheonshim.towerheist.game.GameScreen;
import com.jaeheonshim.towerheist.game.GameWorld;

public class Elevator extends GameObject {
    public static final float scale = 0.3f;

    private Vector2 position;
    private TextureRegion closed;

    public Elevator(GameWorld gameWorld, Vector2 position, int zIndex) {
        super(gameWorld, zIndex);
        this.position = position;
        closed = Assets.instance().fromAtlas("elevator/closed");
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(closed, position.x, position.y, closed.getRegionWidth() / GameScreen.PPM * scale, closed.getRegionHeight() / GameScreen.PPM * scale);
    }
}
