package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.jaeheonshim.towerheist.Assets;
import com.jaeheonshim.towerheist.game.GameScreen;
import com.jaeheonshim.towerheist.game.GameWorld;

public class Key extends GameObject {
    public static final float SCALE = 0.5f;
    private TextureRegion keyTexture;

    private Body body;
    private Vector2 originalPosition = new Vector2(10, 5);

    public Key(GameWorld gameWorld, Vector2 position, int zIndex) {
        super(gameWorld, zIndex);

        keyTexture = Assets.instance().fromAtlas("key");
        this.originalPosition = position;

        setupPhysics(position);
    }

    public void setupPhysics(Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(keyTexture.getRegionWidth() / GameScreen.PPM * SCALE / 2 * 0.8f, keyTexture.getRegionHeight() / GameScreen.PPM * SCALE / 2 * 0.8f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        this.body = gameWorld.getPhysicsWorld().createBody(bodyDef);
        this.body.createFixture(fixtureDef);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void draw(SpriteBatch batch) {
        float width = keyTexture.getRegionWidth() / GameScreen.PPM * SCALE;
        float height = keyTexture.getRegionHeight() / GameScreen.PPM * SCALE;
        batch.draw(keyTexture, body.getPosition().x - width / 2, body.getPosition().y - height / 2, width, height);
    }
}
