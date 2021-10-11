package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jaeheonshim.towerheist.Assets;
import com.jaeheonshim.towerheist.game.GameScreen;
import com.jaeheonshim.towerheist.game.GameWorld;

public class DeathParticles extends GameObject {
    private ParticleEffect effect;
    private boolean running;

    public DeathParticles(GameWorld gameWorld, int zIndex) {
        super(gameWorld, zIndex);

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("death.pe"), Assets.instance().atlas());
        effect.scaleEffect(1 / GameScreen.PPM);
        effect.preAllocateParticles();
    }

    @Override
    public void update(float delta) {
        // Only update particle when effect is running!

        if(effect.isComplete()) {
            running = false;
        }

        if(running) {
            effect.update(delta);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        effect.draw(batch);
    }

    public void doEffect(Vector2 position) {
        effect.setPosition(position.x, position.y);
        effect.start();
        running = true;
    }
}
