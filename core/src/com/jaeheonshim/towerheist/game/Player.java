package com.jaeheonshim.towerheist.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jaeheonshim.towerheist.Assets;
import com.jaeheonshim.towerheist.game.physics.PlayerFixtureUserData;
import com.jaeheonshim.towerheist.util.Countdown;

public class Player {
    public static final float MAX_VELOCITY = 10f;
    public static final float IMPULSE_X = 3;
    public static final int IMPULSE_Y = 10;
    public static final int G_OFFSET = 20;
    public static final float SLIDE_VELOCITY = 0.5f;
    public static final int DECEL = 200;
    public static final float WIDTH = 0.5f;
    public static final int WALL_BOUNCE = 12;
    public static final int WALL_JUMP = 16;

    private GameWorld gameWorld;

    private Body body;

    private boolean touchingGround;
    private boolean touchingWallL;
    private boolean touchingWallR;
    private boolean isMoving;
    private boolean canJump = true;

    // To prevent incorrect state when sliding over two adjacent fixtures
    private Fixture currentFloorFixture;

    private TextureRegion texture;

    private Countdown jumpLeeway = new Countdown(0.08f);
    private Countdown trailCountdown = new Countdown(0.05f);

    public Player(GameWorld world, Vector2 initialPos) {
        this.gameWorld = world;
        this.initPhysics(world.getPhysicsWorld(), initialPos);

        texture = Assets.instance().fromAtlas("player");
    }

    private void initPhysics(World world, Vector2 initialPos) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(initialPos);
        bodyDef.fixedRotation = true;
        bodyDef.allowSleep = false;

        this.body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((WIDTH / 2) * 0.9f, (WIDTH / 2) * 0.9f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 4;
        fixtureDef.friction = 0;

        PolygonShape bottomCollider = new PolygonShape();
        bottomCollider.setAsBox(WIDTH / 2 - 0.05f, 0.05f, new Vector2(0, -(WIDTH / 2 - 0.025f)), 0);

        PolygonShape leftCollider = new PolygonShape();
        leftCollider.setAsBox(0.05f, (WIDTH / 2 - 0.05f), new Vector2(-(WIDTH / 2 - 0.025f), 0.025f), 0);

        PolygonShape rightCollider = new PolygonShape();
        rightCollider.setAsBox(0.05f, (WIDTH / 2) - 0.05f, new Vector2((WIDTH / 2 - 0.025f), 0.025f), 0);


        FixtureDef bottomFixture = new FixtureDef();
        bottomFixture.shape = bottomCollider;
        bottomFixture.isSensor = true;

        FixtureDef leftFixture = new FixtureDef();
        leftFixture.shape = leftCollider;
        leftFixture.isSensor = true;

        FixtureDef rightFixture = new FixtureDef();
        rightFixture.shape = rightCollider;
        rightFixture.isSensor = true;

        body.setUserData(this);
        body.createFixture(fixtureDef).setUserData(new PlayerFixtureUserData(PlayerFixtureUserData.ContactDirection.BODY));
        body.createFixture(bottomFixture).setUserData(new PlayerFixtureUserData(PlayerFixtureUserData.ContactDirection.BOTTOM));
        body.createFixture(leftFixture).setUserData(new PlayerFixtureUserData(PlayerFixtureUserData.ContactDirection.LEFT));
        body.createFixture(rightFixture).setUserData(new PlayerFixtureUserData(PlayerFixtureUserData.ContactDirection.RIGHT));

        shape.dispose();
        bottomCollider.dispose();
        leftCollider.dispose();
        rightCollider.dispose();
    }

    public void update(float delta) {
        trailCountdown.update(delta);
        body.setLinearVelocity(MathUtils.clamp(body.getLinearVelocity().x, -MAX_VELOCITY, MAX_VELOCITY), body.getLinearVelocity().y);

        if(!isMoving && Math.abs(body.getLinearVelocity().x) > 0) {
            body.applyForceToCenter(-(DECEL) * Math.signum(body.getLinearVelocity().x), 0, true);
            if(Math.abs(body.getLinearVelocity().x) < 3.5f) {
                body.setLinearVelocity(0, body.getLinearVelocity().y);
            }
        }

        if(!body.getLinearVelocity().isZero() && trailCountdown.isFinished()) {
            gameWorld.getPlayerTrail().add(getPosition());
            trailCountdown.reset();
        }

        if(!touchingGround) {
            jumpLeeway.update(delta);
        }
    }

    public void draw(SpriteBatch batch) {
        if(gameWorld.isDead()) return;

        batch.draw(texture, body.getPosition().x - (WIDTH / 2), body.getPosition().y - (WIDTH / 2), WIDTH, WIDTH);
    }

    public void move(boolean left) {
        if(gameWorld.isDead()) return;

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
        if(gameWorld.isDead()) return;

        if(!jumpLeeway.isFinished() && canJump) {
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

        if(touchingGround) {
            jumpLeeway.reset();
        }
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

    public Body getBody() {
        return body;
    }

    public Fixture getCurrentFloorFixture() {
        return currentFloorFixture;
    }

    public void setCurrentFloorFixture(Fixture currentFloorFixture) {
        this.currentFloorFixture = currentFloorFixture;
    }
}
