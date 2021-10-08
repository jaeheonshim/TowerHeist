package com.jaeheonshim.bigjaeheon.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.jaeheonshim.bigjaeheon.CannonRayCallback;
import com.jaeheonshim.bigjaeheon.Countdown;
import com.jaeheonshim.bigjaeheon.GameScreen;
import com.jaeheonshim.bigjaeheon.GameWorld;
import com.jaeheonshim.bigjaeheon.game.GameObject;

public class Laser extends GameObject {
    private static final float TRACK_RADIUS = 900;
    private static final float CHARGE_RADIUS = 150;

    private TextureRegion charge0;
    private TextureRegion charge1;
    private TextureRegion charge2;
    private TextureRegion charge3;
    private TextureRegion charge4;
    private TextureRegion armed;
    private TextureRegion laser;

    private ParticleEffect laserParticles;

    private Vector2 position;

    private float theta = 90;
    private int chargeCount;

    private Countdown chargeTimer = new Countdown(1.5f);

    public Laser(GameWorld gameWorld, Vector2 position, int zIndex) {
        super(gameWorld, zIndex);

        this.position = position;

        charge0 = new TextureRegion(new Texture(Gdx.files.internal("laser_charge_0.png")));
        charge1 = new TextureRegion(new Texture(Gdx.files.internal("laser_charge_1.png")));
        charge2 = new TextureRegion(new Texture(Gdx.files.internal("laser_charge_2.png")));
        charge3 = new TextureRegion(new Texture(Gdx.files.internal("laser_charge_3.png")));
        charge4 = new TextureRegion(new Texture(Gdx.files.internal("laser_charge_4.png")));
        armed = new TextureRegion(new Texture(Gdx.files.internal("laser_armed.png")));
        laser = new TextureRegion(new Texture(Gdx.files.internal("laser.png")));

        laserParticles = new ParticleEffect();
        laserParticles.load(Gdx.files.internal("laser.pe"), Gdx.files.internal(""));
        laserParticles.scaleEffect(1 / GameScreen.PPM);
    }

    @Override
    public void update(float delta) {
        chargeTimer.update(delta);
        laserParticles.update(delta);
        Vector2 playerPosition = gameWorld.getPlayer().getPosition();

        if(playerPosition.dst2(position) <= TRACK_RADIUS) {
            theta = MathUtils.radiansToDegrees * MathUtils.atan2(position.y - playerPosition.y, position.x - playerPosition.x);
        }

        if(playerPosition.dst2(position) <= CHARGE_RADIUS && raycast(playerPosition)) {
            if(chargeTimer.isFinished() && !gameWorld.isDead()) {
                charge();
                chargeTimer.reset();
            }
        } else {
            if(chargeTimer.isFinished()) {
                discharge();
                chargeTimer.reset();
            }
        }

        if(chargeCount == 5 && !gameWorld.isDead()) {
            gameWorld.queueDeath();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion draw;

        switch(chargeCount) {
            case 1:
                draw = charge1;
                break;
            case 2:
                draw = charge2;
                break;
            case 3:
                draw = charge3;
                break;
            case 4:
                draw = charge4;
                break;
            case 5:
                draw = armed;
                break;
            default:
                draw = charge0;
                break;
        }

        float width = draw.getRegionWidth() / GameScreen.PPM;
        float height = draw.getRegionHeight() / GameScreen.PPM;

        batch.begin();
        if(chargeCount == 5) {
            drawLaser(batch, width / 2, height / 2);
        }
        batch.draw(draw, position.x - width / 2, position.y - width / 2, width / 2, height / 2, width, height, 1, 1, theta + 90);
        batch.end();
    }

    private void drawLaser(SpriteBatch spriteBatch, float offsetWidth, float offsetHeight) {
        Vector2 playerPosition = gameWorld.getPlayer().getPosition();
        float height = playerPosition.dst(position);
        float width = laser.getRegionHeight() / GameScreen.PPM;

        spriteBatch.setColor(Color.RED);
        laserParticles.start();

        spriteBatch.draw(laser, position.x + (laser.getRegionWidth() / GameScreen.PPM / 2) * MathUtils.sinDeg(theta), position.y - (laser.getRegionWidth() / GameScreen.PPM / 2f) * MathUtils.cosDeg(theta), 0, 0, width, height, 1, 1, theta + 90);

        laserParticles.setPosition(playerPosition.x, playerPosition.y);
        laserParticles.draw(spriteBatch);
        spriteBatch.setColor(Color.WHITE);
    }

    public void discharge() {
        this.chargeCount = Math.max(0, this.chargeCount - 1);
    }

    public void charge() {
        this.chargeCount = Math.min(5, this.chargeCount + 1);
    }

    private boolean raycast(Vector2 player) {
        World world = gameWorld.getPhysicsWorld();
        CannonRayCallback cannonRayCallback = new CannonRayCallback();
        world.rayCast(cannonRayCallback, player, position);

        return cannonRayCallback.isPathExists();
    }

    public void reset() {
        chargeCount = 0;
    }
}
