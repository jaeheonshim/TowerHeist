package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jaeheonshim.towerheist.GameWorld;
import com.jaeheonshim.towerheist.Player;
import com.jaeheonshim.towerheist.game.GameObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlayerTrail extends GameObject {
    public static final float DURATION = 2;
    public static final int MAX_OBJECTS = 100;

    private Map<Vector2, Float> trailElements = new HashMap<>();
    private Texture trail;

    public PlayerTrail(GameWorld gameWorld, int zIndex) {
        super(gameWorld, zIndex);

        trail = new Texture(Gdx.files.internal("trail.png"));
    }

    public void add(Vector2 vector2) {
        if(trailElements.size() < MAX_OBJECTS) {
            trailElements.put(vector2.cpy(), DURATION);
        }
    }

    public void update(float delta) {
        Iterator<Map.Entry<Vector2, Float>> iterator = trailElements.entrySet().iterator();
        Map.Entry<Vector2, Float> entry;
        while(iterator.hasNext()) {
            entry = iterator.next();
            entry.setValue(entry.getValue() - delta);
            if(entry.getValue() <= 0) {
                iterator.remove();
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.begin();
        for(Map.Entry<Vector2, Float> entry : trailElements.entrySet()) {
            batch.setColor(new Color(0xFFFFFF * 256 + (int) (Math.pow(entry.getValue() / DURATION, 2) * 256)));
            batch.draw(trail, entry.getKey().x, entry.getKey().y, Player.WIDTH * 0.5f, Player.WIDTH * 0.5f);
        }
        batch.setColor(Color.WHITE);
        batch.end();
    }
}
