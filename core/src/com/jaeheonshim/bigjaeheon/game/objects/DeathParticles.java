package com.jaeheonshim.bigjaeheon.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jaeheonshim.bigjaeheon.GameScreen;
import com.jaeheonshim.bigjaeheon.GameWorld;
import com.jaeheonshim.bigjaeheon.game.GameObject;

public class DeathParticles extends GameObject {
    private ParticleEffect effect;

    public DeathParticles(GameWorld gameWorld, int zIndex) {
        super(gameWorld, zIndex);

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("death.pe"), Gdx.files.internal(""));
        effect.scaleEffect(1 / GameScreen.PPM);
    }

    @Override
    public void update(float delta) {
        effect.update(delta);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.begin();
        effect.draw(batch);
        batch.end();
    }

    public void doEffect(Vector2 position) {
        effect.setPosition(position.x, position.y);
        effect.start();
    }
}
