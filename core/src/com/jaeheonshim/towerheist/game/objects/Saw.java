package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.jaeheonshim.towerheist.Assets;
import com.jaeheonshim.towerheist.game.GameScreen;
import com.jaeheonshim.towerheist.game.GameWorld;
import com.jaeheonshim.towerheist.game.physics.FixtureType;
import com.jaeheonshim.towerheist.game.physics.FixtureUserData;

public class Saw extends GameObject {
    public static final float ROTATION_RATE = 520;

    private TextureRegion texture;
    private Body body;
    private float theta;

    private Vector2 start;
    private Vector2 end;
    private Interpolation interpolation = Interpolation.smooth;

    private float stateTime;
    private final float lifeTime = 2f;
    private boolean forwards = true;

    public Saw(GameWorld gameWorld, Vector2 position, int zIndex) {
        super(gameWorld, zIndex);
        setupBody(gameWorld, position);

        texture = Assets.instance().fromAtlas("saw");
    }

    public Saw(GameWorld gameWorld, Vector2 start, Vector2 end, int zIndex) {
        this(gameWorld, start, zIndex);
        this.start = start;
        this.end = end;
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
        this.body.createFixture(fixtureDef).setUserData(new FixtureUserData(FixtureType.DEATH));

        shape.dispose();
    }

    public void update(float delta) {
        theta = (theta + ROTATION_RATE * delta) % 360;

        if(start != null) {
            stateTime += delta * (forwards ? 1 : -1);
            float progress = stateTime / lifeTime;

            if((progress >= 1 && forwards) || (progress <= 0 && !forwards)) forwards = !forwards;

            body.setTransform(interpolation.apply(start.x, end.x, progress), interpolation.apply(start.y, end.y, progress), 0);
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, body.getPosition().x - (texture.getRegionWidth() / GameScreen.PPM / 2f), body.getPosition().y - (texture.getRegionHeight() / GameScreen.PPM / 2f), texture.getRegionWidth() / GameScreen.PPM / 2f, texture.getRegionHeight() / GameScreen.PPM / 2f, texture.getRegionWidth() / GameScreen.PPM, texture.getRegionHeight() / GameScreen.PPM, 1, 1, theta);
    }
}
