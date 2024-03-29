package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    public static final float OPEN_DURATION = 1f;
    public static final float COLLIDER_THRESHOLD = 0.5f;

    private TextureRegion doorTexture;
    private TextureRegion doorOpenTexture;

    private Body body;
    private float width;
    private float height;

    private float openHeightPercentage = 0.2f;

    private boolean closed = true;

    private float stateTime = 0;

    private AnimationState animationState = AnimationState.NONE;

    public Door(GameWorld gameWorld, Vector2 position, float height, int zIndex) {
        super(gameWorld, zIndex);
        doorTexture = Assets.instance().fromAtlas("door");
        doorOpenTexture = Assets.instance().fromAtlas("door_open");

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
        this.body.createFixture(fixtureDef).setUserData(new DoorFixtureUserData(this));

        shape.dispose();
    }

    @Override
    public void draw(SpriteBatch batch) {
        float renderHeight;
        float progress = stateTime / OPEN_DURATION;

        if(animationState == AnimationState.NONE) {
            renderHeight = closed ? this.height : height * openHeightPercentage;
        } else if(animationState == AnimationState.OPENING) {
            // opening
            renderHeight = this.height - (1 - openHeightPercentage) * this.height * progress;

            if(progress >= COLLIDER_THRESHOLD) {
                closed = false;
            }
        } else {
            // closing
            renderHeight = this.height * openHeightPercentage + (1 - openHeightPercentage) * this.height * progress;

            if(progress >= COLLIDER_THRESHOLD) {
                closed = true;
            }
        }

        batch.draw(closed ? doorTexture : doorOpenTexture, body.getPosition().x - width / 2, (body.getPosition().y - this.height / 2) + this.height - renderHeight, width, renderHeight);
    }

    @Override
    public void update(float delta) {
        if(animationState != AnimationState.NONE) {
            stateTime += delta;

            if(stateTime >= OPEN_DURATION) {
                animationState = AnimationState.NONE;
                stateTime = 0;
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            if(closed) {
                open();
            } else {
                close();
            }
        }
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void open() {
        animationState = AnimationState.OPENING;
    }

    public void close() {
        animationState = AnimationState.CLOSING;
    }

    enum AnimationState {
        NONE, CLOSING, OPENING
    }
}
