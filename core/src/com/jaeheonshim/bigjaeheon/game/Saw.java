package com.jaeheonshim.bigjaeheon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.jaeheonshim.bigjaeheon.GameScreen;
import com.jaeheonshim.bigjaeheon.GameWorld;

public class Saw extends GameObject {
    public static final float ROTATION_RATE = 520;

    private TextureRegion texture;
    private Vector2 position;
    private float theta;

    public Saw(GameWorld gameWorld, Vector2 position) {
        super(gameWorld);
        this.position = position;

        texture = new TextureRegion(new Texture(Gdx.files.internal("saw.png")));
    }

    public void update(float delta) {
        theta = (theta + ROTATION_RATE * delta) % 360;
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(texture, position.x - (texture.getRegionWidth() / GameScreen.PPM / 2f), position.y - (texture.getRegionHeight() / GameScreen.PPM / 2f), texture.getRegionWidth() / GameScreen.PPM / 2f, texture.getRegionHeight() / GameScreen.PPM / 2f, texture.getRegionWidth() / GameScreen.PPM, texture.getRegionHeight() / GameScreen.PPM, 1, 1, theta);
        spriteBatch.end();
    }
}
