package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.jaeheonshim.towerheist.Assets;
import com.jaeheonshim.towerheist.game.GameScreen;
import com.jaeheonshim.towerheist.game.GameWorld;
import com.jaeheonshim.towerheist.game.physics.DoorFixtureUserData;

public class Door extends GameObject {
    private TextureRegion doorTexture;

    private Body body;
    private float width;
    private float height;

    public Door(GameWorld gameWorld, Vector2 position, float height, int zIndex) {
        super(gameWorld, zIndex);
        doorTexture = Assets.instance().fromAtlas("door");
        this.width = doorTexture.getRegionWidth() / GameScreen.PPM;
        this.height = height;
        setupPhysics(position);
    }

    private void setupPhysics(Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position.add(width / 2, height / 2));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        this.body = gameWorld.getPhysicsWorld().createBody(bodyDef);
        this.body.createFixture(fixtureDef).setUserData(new DoorFixtureUserData());
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(doorTexture, body.getPosition().x - width / 2, body.getPosition().y - height / 2, width, height);
        batch.end();
    }
}
