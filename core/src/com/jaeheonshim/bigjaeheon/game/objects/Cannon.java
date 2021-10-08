package com.jaeheonshim.bigjaeheon.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Cannon extends GameObject {
    private static final float TRACK_RADIUS = 900;
    private static final float ARM_RADIUS = 150;

    private TextureRegion safeTexture;
    private TextureRegion armedTexture;

    private Vector2 position;
    private float theta;

    private boolean armed = false;
    private int zIndex;

    private List<Bullet> bullets = new LinkedList<>();

    private Countdown fireCountdown = new Countdown(1.25f);

    public Cannon(GameWorld gameWorld, Vector2 position, int zIndex) {
        super(gameWorld, zIndex);
        this.zIndex = zIndex;

        this.position = position;

        safeTexture = new TextureRegion(new Texture(Gdx.files.internal("cannon_safe.png")));
        armedTexture = new TextureRegion(new Texture(Gdx.files.internal("cannon_armed.png")));
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(armed ? armedTexture : safeTexture, position.x - safeTexture.getRegionWidth() / GameScreen.PPM / 2, position.y - safeTexture.getRegionHeight() / GameScreen.PPM / 2, safeTexture.getRegionWidth() / GameScreen.PPM / 2, safeTexture.getRegionHeight() / GameScreen.PPM / 2, safeTexture.getRegionWidth() / GameScreen.PPM, safeTexture.getRegionHeight() / GameScreen.PPM, 1, 1, theta + 90);
        batch.end();

        for(Bullet bullet : bullets) {
            bullet.draw(batch);
        }
    }

    @Override
    public void update(float delta) {
        Vector2 playerPosition = gameWorld.getPlayer().getPosition();

        if(playerPosition.dst2(position) <= TRACK_RADIUS) {
            theta = MathUtils.radiansToDegrees * MathUtils.atan2(position.y - playerPosition.y, position.x - playerPosition.x);
        }

        if(playerPosition.dst2(position) <= ARM_RADIUS) {
            if(raycast(playerPosition)) {
                armed = true;
            }
        } else {
            armed = false;
        }

        if(armed && raycast(playerPosition)) {
            if(fireCountdown.isFinished()) {
                bullets.add(new Bullet(gameWorld, position.cpy().add(MathUtils.cosDeg((theta + 180) % 360) * 0.7f, MathUtils.sinDeg((theta + 180) % 360) * 0.7f), theta + 180, zIndex));
                fireCountdown.reset();
            } else {
                fireCountdown.update(delta);
            }
        }

        Iterator<Bullet> bulletIterator = bullets.listIterator();
        Bullet bullet;
        while(bulletIterator.hasNext()) {
            bullet = bulletIterator.next();
            if(bullet.isQueueDestroy()) {
                bullet.destroy();
                bullet.dispose();
                bulletIterator.remove();
            }
        }
    }

    private boolean raycast(Vector2 player) {
        World world = gameWorld.getPhysicsWorld();
        CannonRayCallback cannonRayCallback = new CannonRayCallback();
        world.rayCast(cannonRayCallback, player, position);

        return cannonRayCallback.isPathExists();
    }
}
