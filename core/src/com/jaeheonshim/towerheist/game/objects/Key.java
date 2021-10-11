package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
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
import com.jaeheonshim.towerheist.game.physics.FixtureClass;
import com.jaeheonshim.towerheist.game.physics.FixtureType;
import com.jaeheonshim.towerheist.game.physics.FixtureUserData;
import com.jaeheonshim.towerheist.game.physics.KeyFixtureUserData;

public class Key extends GameObject implements Carryable {
    public static final float SCALE = 0.5f;
    public static final float LERP = 0.4f;

    public static final float MOVING_ANGLE = 60f;
    public static final float CARRY_ANGLE = 45f;

    private TextureRegion keyTexture;

    private Body body;
    private Vector2 originalPosition = new Vector2(10, 5);

    private Interpolation interpolation = Interpolation.bounce;
    private Vector2 targetPosition;

    private boolean isCarried;

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
        bodyDef.gravityScale = 0;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(keyTexture.getRegionWidth() / GameScreen.PPM * SCALE / 2 * 0.8f, keyTexture.getRegionHeight() / GameScreen.PPM * SCALE / 2 * 0.8f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        this.body = gameWorld.getPhysicsWorld().createBody(bodyDef);
        this.body.createFixture(fixtureDef).setUserData(new KeyFixtureUserData(this));
    }

    @Override
    public void update(float delta) {
        if(isCarried) {
            targetPosition = gameWorld.getPlayer().getPosition().add(0, 1.5f);
        } else {
            targetPosition = originalPosition;
        }

        float x = interpolation.apply(body.getPosition().x, targetPosition.x, LERP);
        float y = interpolation.apply(body.getPosition().y, targetPosition.y, LERP);

        float angle = gameWorld.getPlayer().isMoving() && isCarried ? (interpolation.apply(body.getAngle(), MOVING_ANGLE, LERP)) : (interpolation.apply(body.getAngle(), CARRY_ANGLE, LERP));

        body.setTransform(x, y, angle);
    }

    @Override
    public void draw(SpriteBatch batch) {
        float width = keyTexture.getRegionWidth() / GameScreen.PPM * SCALE;
        float height = keyTexture.getRegionHeight() / GameScreen.PPM * SCALE;
        batch.draw(keyTexture, body.getPosition().x - width / 2, body.getPosition().y - height / 2, width / 2, height / 2, width, height, 1, 1, body.getAngle());
    }

    @Override
    public void beginCarry() {
        isCarried = true;
    }

    @Override
    public void endCarry() {
        isCarried = false;
    }
}
