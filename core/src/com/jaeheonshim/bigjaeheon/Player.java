package com.jaeheonshim.bigjaeheon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player {
    public static final float MAX_VELOCITY = 12f;
    public static final float IMPULSE_X = 3;
    public static final int IMPULSE_Y = 14;
    public static final int G_OFFSET = 20;
    public static final float SLIDE_VELOCITY = 0.5f;
    public static final int DECEL = 110;
    public static final float WIDTH = 0.5f;
    public static final int WALL_BOUNCE = 12;
    public static final int WALL_JUMP = 18;

    private GameWorld gameWorld;

    private Body body;

    private boolean touchingGround;
    private boolean touchingWallL;
    private boolean touchingWallR;
    private boolean isMoving;
    private boolean canJump = true;

    private Texture texture;

    private Countdown trailCountdown = new Countdown(0.05f);

    public Player(GameWorld world, Vector2 initialPos) {
        this.gameWorld = world;
        this.initPhysics(world.getPhysicsWorld(), initialPos);

        texture = new Texture(Gdx.files.internal("player.png"));
    }

    private void initPhysics(World world, Vector2 initialPos) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(initialPos);
        bodyDef.fixedRotation = true;

        this.body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH / 2, WIDTH / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 4;
        fixtureDef.friction = 0;

        body.setUserData(this);
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void update(float delta) {
        trailCountdown.update(delta);
        body.setLinearVelocity(MathUtils.clamp(body.getLinearVelocity().x, -MAX_VELOCITY, MAX_VELOCITY), body.getLinearVelocity().y);

        if(!isMoving && Math.abs(body.getLinearVelocity().x) > 0) {
            body.applyForceToCenter(-(DECEL) * Math.signum(body.getLinearVelocity().x), 0, true);
            if(Math.abs(body.getLinearVelocity().x) < 1.5f) {
                body.setLinearVelocity(0, body.getLinearVelocity().y);
            }
        }

        if(!body.getLinearVelocity().isZero() && trailCountdown.isFinished()) {
            gameWorld.getPlayerTrail().add(getPosition());
            trailCountdown.reset();
        }
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, body.getPosition().x - (WIDTH / 2), body.getPosition().y - (WIDTH / 2), WIDTH, WIDTH);
        batch.end();
    }

    public void move(boolean left) {
        float coef = isTouchingGround() ? 1 : 0.5f;
        isMoving = true;
        body.applyLinearImpulse(IMPULSE_X * coef * (left ? -1: 1), 0, 0, 0, true);

        if((touchingWallR && left || touchingWallL && !left) && body.getLinearVelocity().y < 0) {
            body.setLinearVelocity(body.getLinearVelocity().x, -SLIDE_VELOCITY);
        }
    }

    public void stop() {
        isMoving = false;
    }

    public void jump() {
        if(isTouchingGround() && canJump) {
            body.applyLinearImpulse(0, IMPULSE_Y, 0, 0, true);
            canJump = false;
            setTouchingGround(false);
        }

        if(touchingWallL && canJump) {
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(-WALL_BOUNCE, WALL_JUMP, 0, 0, true);
            canJump = false;
            setTouchingGround(false);
        }

        if(touchingWallR && canJump) {
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(WALL_BOUNCE, WALL_JUMP, 0, 0, true);
            canJump = false;
            setTouchingGround(false);
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

    public void setTouchingWallL(boolean touchingWallL) {
        this.touchingWallL = touchingWallL;
    }

    public void setTouchingWallR(boolean touchingWallR) {
        this.touchingWallR = touchingWallR;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void setPosition(Vector2 newPosition) {
        body.setTransform(newPosition, 0);
    }
}
