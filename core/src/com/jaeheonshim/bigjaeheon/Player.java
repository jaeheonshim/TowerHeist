package com.jaeheonshim.bigjaeheon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;

public class Player {
    public static final float MAX_VELOCITY = 18f;
    public static final float IMPULSE_X = 3;
    public static final int IMPULSE_Y = 10;
    public static final int G_OFFSET = 20;
    public static final int DECEL = 80;

    private Body body;

    private boolean touchingGround;
    private boolean isMoving;

    private Texture texture;

    public Player(World world) {
        this.initPhysics(world);

        texture = new Texture(Gdx.files.internal("player.png"));
    }

    private void initPhysics(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(5, 10);
        bodyDef.fixedRotation = true;

        this.body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 0;

        body.setUserData(this);
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void update() {
        body.setLinearVelocity(MathUtils.clamp(body.getLinearVelocity().x, -MAX_VELOCITY, MAX_VELOCITY), body.getLinearVelocity().y);

        if(!isMoving && Math.abs(body.getLinearVelocity().x) > 0) {
            body.applyForceToCenter(-DECEL * Math.signum(body.getLinearVelocity().x), 0, true);
            if(Math.abs(body.getLinearVelocity().x) < 0.5) {
                body.setLinearVelocity(0, body.getLinearVelocity().y);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, body.getPosition().x - (texture.getWidth() / GameScreen.PPM / 2), body.getPosition().y - (texture.getHeight() / GameScreen.PPM / 2), texture.getWidth() / GameScreen.PPM, texture.getHeight() / GameScreen.PPM);
        batch.end();
    }

    public void move(boolean left) {
        isMoving = true;
        body.applyLinearImpulse(IMPULSE_X * (left ? -1: 1), 0, 0, 0, true);
    }

    public void stop() {
        isMoving = false;
    }

    public void jump() {
        if(isTouchingGround()) {
            body.applyLinearImpulse(0, IMPULSE_Y, 0, 0, true);
        }

        if(body.getLinearVelocity().y > 0) {
            body.applyForceToCenter(0, G_OFFSET, true);
        }
    }

    public boolean isTouchingGround() {
        return touchingGround;
    }

    public void setTouchingGround(boolean touchingGround) {
        this.touchingGround = touchingGround;
    }
}
