package com.jaeheonshim.towerheist;

import com.badlogic.gdx.physics.box2d.*;
import com.jaeheonshim.towerheist.game.objects.Bullet;
import com.jaeheonshim.towerheist.game.objects.Checkpoint;

import java.util.Objects;

public class WorldContactListener implements ContactListener {
    private GameWorld world;

    public WorldContactListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();
        Player player;

        Fixture fixture;

        if(checkXOR(contact, "PLAYER.BOTTOM") != null && checkXOR(contact, "FLOOR") != null) {
            world.getPlayer().setTouchingGround(true);
        }

        if(checkXOR(contact, "PLAYER.LEFT") != null && checkXOR(contact, "FLOOR") != null) {
            world.getPlayer().setTouchingWallR(true);
        }

        if(checkXOR(contact, "PLAYER.RIGHT") != null && checkXOR(contact, "FLOOR") != null) {
            world.getPlayer().setTouchingWallL(true);
        }

        if(checkXOR(contact, "DEATH") != null && (player = getPlayer(contact)) != null) {
            world.queueDeath();
        }

        if((fixture = checkXOR(contact, "BULLET")) != null) {
            if((player = getPlayer(contact)) != null) {
                world.queueDeath();
            }

            Bullet bullet = ((Bullet) fixture.getBody().getUserData());
            bullet.setQueueDestroy(true);
        }

        if((fixture = checkXOR(contact, "CHECKPOINT")) != null && (player = getPlayer(contact)) != null) {
            world.setCurrentCheckpoint(((Checkpoint) fixture.getBody().getUserData()));
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        if(checkXOR(contact, "PLAYER.BOTTOM") != null && checkXOR(contact, "FLOOR") != null) {
            world.getPlayer().setTouchingGround(false);
        }

        if(checkXOR(contact, "PLAYER.LEFT") != null && checkXOR(contact, "FLOOR") != null) {
            world.getPlayer().setTouchingWallR(false);
        }

        if(checkXOR(contact, "PLAYER.RIGHT") != null && checkXOR(contact, "FLOOR") != null) {
            world.getPlayer().setTouchingWallL(false);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public Player getPlayer(Contact contact) {
        if(contact.getFixtureA().getBody().getUserData() != null && contact.getFixtureA().getBody().getUserData() instanceof Player) {
            return (Player) contact.getFixtureA().getBody().getUserData();
        }

        if(contact.getFixtureB().getBody().getUserData() != null && contact.getFixtureB().getBody().getUserData() instanceof Player) {
            return (Player) contact.getFixtureB().getBody().getUserData();
        }

        return null;
    }

    public Fixture checkXOR(Contact contact, Object userData) {
        if(Objects.equals(contact.getFixtureA().getUserData(), userData) && Objects.equals(contact.getFixtureB().getUserData(), userData)) {
            return null;
        }

        if(Objects.equals(contact.getFixtureA().getUserData(), userData)) {
            return contact.getFixtureA();
        }

        if(Objects.equals(contact.getFixtureB().getUserData(), userData)) {
            return contact.getFixtureB();
        }

        return null;
    }
}
