package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.jaeheonshim.towerheist.Assets;
import com.jaeheonshim.towerheist.game.GameScreen;
import com.jaeheonshim.towerheist.game.GameWorld;
import com.jaeheonshim.towerheist.game.physics.BulletFixtureUserData;

public class Bullet extends GameObject implements Disposable {
    private static final float VELOCITY = 15;

    private TextureRegion bullet;

    private Body body;
    private Fixture fixture;
    private boolean queueDestroy;

    public Bullet(GameWorld gameWorld, Vector2 position, float theta, int zIndex) {
        super(gameWorld, zIndex);
        bullet = Assets.instance().fromAtlas("bullet");
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

        shape.dispose();
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(bullet, body.getPosition().x - bullet.getRegionWidth() / GameScreen.PPM / 2, body.getPosition().y - bullet.getRegionHeight() / GameScreen.PPM / 2, bullet.getRegionWidth() / GameScreen.PPM, bullet.getRegionHeight() / GameScreen.PPM);
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

    }
}
