package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.jaeheonshim.towerheist.GameScreen;
import com.jaeheonshim.towerheist.GameWorld;
import com.jaeheonshim.towerheist.game.GameObject;

public class Saw extends GameObject {
    public static final float ROTATION_RATE = 520;

    private TextureRegion texture;
    private Body body;
    private float theta;

    public Saw(GameWorld gameWorld, Vector2 position, int zIndex) {
        super(gameWorld, zIndex);
        setupBody(gameWorld, position);

        texture = new TextureRegion(new Texture(Gdx.files.internal("saw.png")));
    }

    private void setupBody(GameWorld gameWorld, Vector2 position) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.KinematicBody;
        def.position.set(position);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        this.body = gameWorld.getPhysicsWorld().createBody(def);
        this.body.createFixture(fixtureDef).setUserData("DEATH");
    }

    public void update(float delta) {
        theta = (theta + ROTATION_RATE * delta) % 360;
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(texture, body.getPosition().x - (texture.getRegionWidth() / GameScreen.PPM / 2f), body.getPosition().y - (texture.getRegionHeight() / GameScreen.PPM / 2f), texture.getRegionWidth() / GameScreen.PPM / 2f, texture.getRegionHeight() / GameScreen.PPM / 2f, texture.getRegionWidth() / GameScreen.PPM, texture.getRegionHeight() / GameScreen.PPM, 1, 1, theta);
        spriteBatch.end();
    }
}
