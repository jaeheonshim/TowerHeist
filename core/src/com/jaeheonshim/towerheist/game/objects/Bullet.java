package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.jaeheonshim.towerheist.GameScreen;
import com.jaeheonshim.towerheist.GameWorld;
import com.jaeheonshim.towerheist.game.GameObject;
import com.jaeheonshim.towerheist.game.physics.BulletFixtureUserData;
import com.jaeheonshim.towerheist.game.physics.FixtureUserData;

public class Bullet extends GameObject implements Disposable {
    private static final float VELOCITY = 15;

    private Texture bullet;

    private Body body;
    private Fixture fixture;
    private boolean queueDestroy;

    public Bullet(GameWorld gameWorld, Vector2 position, float theta, int zIndex) {
        super(gameWorld, zIndex);
        bullet = new Texture(Gdx.files.internal("bullet.png"));
        setupPhysics(gameWorld.getPhysicsWorld(), position);
        body.setLinearVelocity(MathUtils.cosDeg(theta) * VELOCITY, MathUtils.sinDeg(theta) * VELOCITY);
    }

    private void setupPhysics(World world, Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale = 0;

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(0.2f);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        this.body = world.createBody(bodyDef);
        this.fixture = this.body.createFixture(fixtureDef);
        fixture.setUserData(new BulletFixtureUserData(this));
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(bullet, body.getPosition().x - bullet.getWidth() / GameScreen.PPM / 2, body.getPosition().y - bullet.getHeight() / GameScreen.PPM / 2, bullet.getWidth() / GameScreen.PPM, bullet.getHeight() / GameScreen.PPM);
        batch.end();
    }

    public void setQueueDestroy(boolean queueDestroy) {
        this.queueDestroy = queueDestroy;
    }

    public void destroy() {
        for (Fixture fixture : body.getFixtureList()) {
            body.destroyFixture(fixture);
        }
    }

    public boolean isQueueDestroy() {
        return queueDestroy;
    }

    @Override
    public void dispose() {
        bullet.dispose();
    }
}
