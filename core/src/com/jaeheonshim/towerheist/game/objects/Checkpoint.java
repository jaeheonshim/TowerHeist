package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jaeheonshim.towerheist.*;
import com.jaeheonshim.towerheist.game.GameObject;
import com.jaeheonshim.towerheist.game.physics.CheckpointFixtureUserData;

public class Checkpoint extends GameObject {
    private int id;
    private Body body;
    private float height;

    private TextureRegion unset;
    private TextureRegion set;

    public Checkpoint(GameWorld gameWorld, Rectangle rectangle, int id, int zIndex) {
        super(gameWorld, zIndex);
        this.id = id;
        unset = Assets.instance().fromAtlas("checkpoint_unset");
        set = Assets.instance().fromAtlas("checkpoint_set");

        configPhysics(gameWorld.getPhysicsWorld(), rectangle);
        this.height = rectangle.height;
    }

    public void configPhysics(World world, Rectangle rectangle) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(rectangle.x, rectangle.y);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rectangle.width / 4f, rectangle.height / 2f, new Vector2(rectangle.width / 2f, rectangle.height / 2), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        this.body = world.createBody(def);
        this.body.setUserData(this);
        this.body.createFixture(fixtureDef).setUserData(new CheckpointFixtureUserData(this));
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(gameWorld.getCurrentCheckpoint() == this ? set : unset, body.getPosition().x, body.getPosition().y, set.getRegionWidth() / GameScreen.PPM, height);
        spriteBatch.end();
    }

    public int getId() {
        return id;
    }

    public Body getBody() {
        return body;
    }
}
